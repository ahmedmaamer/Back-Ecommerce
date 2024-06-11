package com.example.iotDataGenerator.service;

import com.example.iotDataGenerator.entities.Attribute;
import com.example.iotDataGenerator.entities.AttributeGenerationRequest;
import com.example.iotDataGenerator.entities.Device;
import com.example.iotDataGenerator.enums.Topic;
import com.example.iotDataGenerator.repository.AttributeRepository;
import com.example.iotDataGenerator.repository.DeviceDataRepository;
import com.example.iotDataGenerator.repository.DeviceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.DateAndTime;
import com.github.javafaker.Faker;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class AttributeGeneratorService {

    private final AttributeRepository attributeRepository;
    private final DeviceDataRepository deviceDataRepository;
    private final Faker faker = new Faker();

    private static final Logger logger = LoggerFactory.getLogger(AttributeGeneratorService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private MeterRegistry meterRegistry ;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TaskScheduler taskScheduler;
    private final Counter dataCounter;

    private String kafkaTopic;
    @Autowired
    public AttributeGeneratorService(AttributeRepository attributeRepository, DeviceDataRepository deviceDataRepository , KafkaTemplate<String, String> kafkaTemplate , TaskScheduler taskScheduler , MeterRegistry meterRegistry) {
        this.attributeRepository = attributeRepository;
        this.deviceDataRepository = deviceDataRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.taskScheduler = taskScheduler;
        this.dataCounter = Counter.builder("data_generation_count")
                .description("Number of data points generated")
                .register(meterRegistry);
    }

    public void scheduleDataGeneration(Long deviceId, List<AttributeGenerationRequest> attributeGenerationRequests, int maxDataPoints, long duration, int numberOfCycles) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        Device device = deviceOptional.orElseThrow(() -> new IllegalArgumentException("Device not found"));
        device.setConfigurationJson(toString(attributeGenerationRequests));
        device.setPeriod(duration);
        device.setCycles(numberOfCycles);
        device.setMawDataPoints(maxDataPoints);
        device.setStatus("active");
        deviceRepository.save(device);
        logger.info("device updated");

        AtomicInteger cyclesLeft = new AtomicInteger(numberOfCycles);

        ThreadPoolTaskScheduler threadPoolTaskScheduler = (ThreadPoolTaskScheduler) taskScheduler;
        ScheduledFuture<?>[] scheduledFuture = new ScheduledFuture<?>[1];

        scheduledFuture[0] = threadPoolTaskScheduler.scheduleAtFixedRate(() -> {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    Timer.Sample sample = Timer.start(meterRegistry);
                    generateDataForAttributes(deviceId, attributeGenerationRequests, maxDataPoints);
                    sample.stop(Timer.builder("data_generation_duration")
                            .tag("device_id", String.valueOf(deviceId))
                            .description("Time taken for data generation")
                            .register(meterRegistry));

                    int remainingCycles = cyclesLeft.decrementAndGet();
                    if (remainingCycles <= 0) {
                        // Cancel the scheduled task after the specified number of cycles
                        if (scheduledFuture[0] != null && !scheduledFuture[0].isCancelled()) {
                            boolean result = scheduledFuture[0].cancel(false);
                            logger.info("Scheduled task cancellation result: {}", result);
                            device.setStatus("inactive");
                            deviceRepository.save(device);
                        }
                    }
                }
            });
        }, Duration.ofMillis(duration));
    }



    public void generateDataForAttributes(Long deviceId, List<AttributeGenerationRequest> attributeGenerationRequests, int maxDataPoints) {


        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        Device device = deviceOptional.orElseThrow(() -> new IllegalArgumentException("Device not found"));

        List<String> attributeNames = attributeGenerationRequests.stream()
                .map(AttributeGenerationRequest::getAttributeName)
                .collect(Collectors.toList());
        String tableName = (device.getDeviceName() + "_" + deviceId).replaceAll("[^a-zA-Z0-9_]", "_");

        createDynamicTable(device.getDeviceName(), device.getDeviceId(), attributeNames);
        logger.info("Data generation started for device {} at {}", deviceId, LocalDateTime.now());
        logger.info("Generating data for attributes with maxDataPoints: {}", maxDataPoints);

        for (int v = 0; v < maxDataPoints; v++) {
            try {
                StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
                queryBuilder.append(tableName).append(" (deviceName, deviceType, ");

                // Append attribute names as columns
                String columnNames = attributeNames.stream().collect(Collectors.joining(", "));
                queryBuilder.append(columnNames);

                queryBuilder.append(") VALUES (?, ?, ");

                // Add placeholders for attribute values
                String valuePlaceholders = attributeNames.stream().map(attr -> "?").collect(Collectors.joining(", "));
                queryBuilder.append(valuePlaceholders);

                queryBuilder.append(")");

                Query query = entityManager.createNativeQuery(queryBuilder.toString());

                query.setParameter(1, device.getDeviceName());
                query.setParameter(2, device.getDeviceType());

                Random random = new Random();
                List<String> generatedValues = new ArrayList<>();

                for (String attributeName : attributeNames) {
                    logger.info("Generating data for sorted attribute: {}", attributeName);
                    AttributeGenerationRequest request = findAttributeGenerationRequest(attributeGenerationRequests, attributeName);
                    if (request != null) {
                        Attribute attribute = attributeRepository.findByDevice_deviceIdAndAttributeName(deviceId, request.getAttributeName());
                        if (attribute != null) {
                            attribute.setGenerationMethod(request.getGenerationMethod());
                            attribute.setStringGenerationMethod(request.getStringGenerationMethod());
                            attribute.setDateGenerationMethod(request.getDateGenerationMethod());

                            String generatedValue = generateValueForAttribute(attribute, random, request.getMinValue(), request.getMaxValue(),
                                    request.getSpecifiedValues(), request.getRanges(), request.getSpecifiedStringValues(), request.getPattern(), request.getFirstDate(), request.getLastDate(), request.getSpecifiedDates());
                            generatedValues.add(generatedValue);

                            logger.info("Attribute data inserted successfully for device {}", deviceId);
                        } else {
                            logger.warn("Attribute '{}' not found for device '{}'", request.getAttributeName(), deviceId);
                        }
                    }
                }
                for (int j = 0; j < generatedValues.size(); j++) {
                    query.setParameter(j + 3, generatedValues.get(j));
                }

                query.executeUpdate();
                // Construct message using already generated values
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceId", deviceId);
                jsonObject.put("deviceName", device.getDeviceName());
                jsonObject.put("deviceType", device.getDeviceType());

                // Construct attribute data
                for (int j = 0; j < generatedValues.size(); j++) {
                    String attributeName = attributeNames.get(j);
                    String generatedValue = generatedValues.get(j);
                    jsonObject.put(attributeName, generatedValue);
                }

                // Convert JSON object to string
                String jsonString = jsonObject.toString();

                // Send the JSON string to Kafka
                kafkaTemplate.send(Topic.DATAGENERATION.getTopic(), jsonString);
                dataCounter.increment();

                logger.info("SENT: {}", jsonString);
                System.out.println(jsonString);
                logger.info("Data generation completed for device {} at {}", deviceId, LocalDateTime.now());

            } catch (Exception e) {
                logger.error("Failed to generate attribute data", e);
            }
        }
    }
    public Map<String, Map<String, Object>> getTop3GeneratedDataStatistics(Long deviceId, String deviceName) {
        Map<String, Map<String, Object>> attributeStatistics = getGeneratedDataStatistics(deviceId, deviceName);

        // Iterate over attribute statistics and keep only the top 3 percentages for each attribute
        attributeStatistics.forEach((attributeName, attributeData) -> {
            Map<String, Double> valuePercentages = (Map<String, Double>) attributeData.get("valuePercentages");
            if (valuePercentages != null) {
                Map<String, Double> top3Percentages = valuePercentages.entrySet().stream()
                        .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                        .limit(3)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

                attributeData.put("top3Percentages", top3Percentages);
            }
        });

        return attributeStatistics;
    }
    public Map<String, Map<String, Object>> getGeneratedDataStatistics(Long deviceId, String deviceName) {
        Map<String, Map<String, Object>> attributeStatistics = new HashMap<>();
        String tableName = (deviceName + "_" + deviceId).replaceAll("[^a-zA-Z0-9_]", "_");
        String jdbcUrl = "jdbc:postgresql://localhost:5432/ecommerce";
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            Map<String, Map<String, Integer>> attributeValueCounts = new HashMap<>();
            Map<String, Integer> totalCounts = new HashMap<>();

            // Initialize maps for each attribute
            for (int i = 3; i <= columnCount; i++) { // Skip first two columns (deviceName, deviceType)
                String columnName = metaData.getColumnName(i);
                attributeValueCounts.put(columnName, new HashMap<>());
                totalCounts.put(columnName, 0);
            }

            // Count occurrences of each value for each attribute
            while (resultSet.next()) {
                for (int i = 3; i <= columnCount; i++) { // Skip first two columns (deviceName, deviceType)
                    String columnName = metaData.getColumnName(i);
                    String value = resultSet.getString(i);

                    Map<String, Integer> valueCounts = attributeValueCounts.get(columnName);
                    valueCounts.put(value, valueCounts.getOrDefault(value, 0) + 1);
                    totalCounts.put(columnName, totalCounts.get(columnName) + 1);
                }
            }

            // Calculate percentages and prepare the result
            for (String attributeName : attributeValueCounts.keySet()) {
                Map<String, Integer> valueCounts = attributeValueCounts.get(attributeName);
                int totalCount = totalCounts.get(attributeName);

                Map<String, Object> attributeData = new HashMap<>();
                attributeData.put("totalCount", totalCount);
                attributeData.put("valueCounts", valueCounts);

                Map<String, Double> valuePercentages = new HashMap<>();
                for (String value : valueCounts.keySet()) {
                    int count = valueCounts.get(value);
                    double percentage = (count * 100.0) / totalCount;
                    valuePercentages.put(value, percentage);
                }
                attributeData.put("valuePercentages", valuePercentages);

                attributeStatistics.put(attributeName, attributeData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attributeStatistics;
    }

    // New method to calculate the percentage difference for each attribute
    public Map<String, Double> getPercentageDifferences(Long deviceId, String deviceName) {
        Map<String, Map<String, Object>> attributeStatistics = getGeneratedDataStatistics(deviceId, deviceName);
        Map<String, Double> percentageDifferences = new HashMap<>();

        attributeStatistics.forEach((attributeName, attributeData) -> {
            Map<String, Double> valuePercentages = (Map<String, Double>) attributeData.get("valuePercentages");
            if (valuePercentages != null && valuePercentages.size() > 1) {
                double maxPercentage = Collections.max(valuePercentages.values());
                double minPercentage = Collections.min(valuePercentages.values());
                double percentageDifference = maxPercentage - minPercentage;
                percentageDifferences.put(attributeName, percentageDifference);
            }
        });

        return percentageDifferences;
    }



    public double getAveragePercentageDifference(Long deviceId, String deviceName) {
        Map<String, Map<String, Object>> attributeStatistics = getGeneratedDataStatistics(deviceId, deviceName);

        List<Double> percentageRanges = new ArrayList<>();

        attributeStatistics.forEach((attributeName, attributeData) -> {
            Map<String, Double> valuePercentages = (Map<String, Double>) attributeData.get("valuePercentages");
            if (valuePercentages != null && valuePercentages.size() > 1) {
                double maxPercentage = Collections.max(valuePercentages.values());
                double minPercentage = Collections.min(valuePercentages.values());
                double percentageRange = maxPercentage - minPercentage;
                percentageRanges.add(percentageRange);
            }
        });

        if (percentageRanges.isEmpty()) {
            return 0.0; // No attributes with multiple values, return 0 as default
        }

        // Calculate average percentage difference
        double sumPercentageRanges = percentageRanges.stream().mapToDouble(Double::doubleValue).sum();
        return sumPercentageRanges / percentageRanges.size();
    }

    private AttributeGenerationRequest findAttributeGenerationRequest(List<AttributeGenerationRequest> attributeGenerationRequests, String attributeName) {
        return attributeGenerationRequests.stream()
                .filter(request -> request.getAttributeName().equals(attributeName))
                .findFirst()
                .orElse(null);
    }

    public void createDynamicTable(String tableName, Long deviceId, List<String> attributeNames) {
        String sanitizedTableName = (tableName + "_" + deviceId).replaceAll("[^a-zA-Z0-9_]", "_");

        String jdbcUrl = "jdbc:postgresql://localhost:5432/ecommerce";
        String username = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("CREATE TABLE ").append(sanitizedTableName).append(" (");
            sqlBuilder.append("deviceName VARCHAR(255), ");
            sqlBuilder.append("deviceType VARCHAR(255), ");

            for (String attributeName : attributeNames) {
                sqlBuilder.append(attributeName).append(" VARCHAR(255), ");
            }

            sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
            sqlBuilder.append(")");

            String sql = sqlBuilder.toString();

            statement.executeUpdate(sql);

            logger.info("Dynamic table created successfully: {}", sanitizedTableName);
        } catch (SQLException e) {
            logger.error("Failed to create dynamic table", e);
        }
    }



    private String generateValueForAttribute(Attribute attribute, Random random, int minValue, int maxValue, List<Integer> specifiedValues , List<Pair<Integer, Integer>> ranges ,  List<String> specifiedStringValues , String pattern , Date firstDate , Date lastDate, List<String > specifiedDates ) {
        switch (attribute.getDataType()) {
            case "String":
                return generateStringAttribute(attribute, random ,specifiedStringValues, pattern);
            case "Integer":
                return generateValueForIntegerAttribute( attribute,  random,  minValue,  maxValue,  specifiedValues,  ranges);
            case "Boolean":
                return String.valueOf(random.nextBoolean());
            case "Date":
                return generateDateAttributeValue(attribute, firstDate, lastDate , specifiedDates);
            default:
                return "";
        }
    }

    private String generateDateAttributeValue(Attribute attribute , Date firstDate,Date lastDate, List<String > specifiedDates) {
        DateAndTime dateAndTime = new Faker().date();


        switch (attribute.getDateGenerationMethod()) {
            case "Range Between Dates":
                return dateAndTime.between(firstDate, lastDate).toString();
            case "List Of Dates":
                if (!specifiedDates.isEmpty()) {
                    String date = specifiedDates.get(new Random().nextInt(specifiedDates.size()));
                    return date;
                } else {
                    return ""; // Handle case when the list is empty
                }

            default:
                return ""; // Handle invalid generation method
        }
    }
    private String generateValueForIntegerAttribute(Attribute attribute, Random random, int minValue, int maxValue, List<Integer> specifiedValues, List<Pair<Integer, Integer>> ranges) {
        if (attribute == null) {
            return "";
        }

        switch (attribute.getGenerationMethod()) {
            case "Random":
                return generateRandomIntegerValue(random, minValue, maxValue);
            case "RandomLimited":
                return generateRandomLimitedIntegerValue(random, minValue, maxValue);
            case "RandomLimitedPlusSpecified":
                return generateRandomLimitedPlusSpecifiedIntegerValue(random, minValue, maxValue, specifiedValues);
            case "Random List":
                return generateRandomIntegerValueFromList(random, specifiedValues);
            case "RandomMultipleRanges":
                return generateRandomIntegerValueFromMultipleRanges(random, ranges);
            case "RandomMultipleRangesPlusSpecified":
                return generateRandomIntegerValueFromMultipleRangesPlusSpecified(random, ranges, specifiedValues);
            // Add cases for other generation methods as needed
            default:
                return "";
        }
    }


    private String generateRandomIntegerValue(Random random, int minValue, int maxValue) {
        return String.valueOf(random.nextInt(maxValue - minValue + 1) + minValue);
    }

    private String generateRandomLimitedIntegerValue(Random random, int minValue, int maxValue) {
        return String.valueOf(random.nextInt(maxValue - minValue + 1) + minValue);
    }

    private String generateRandomLimitedPlusSpecifiedIntegerValue(Random random, int minValue, int maxValue, List<Integer> specifiedValues) {
        int randomValue = random.nextInt(maxValue - minValue + 1) + minValue;
        if (!specifiedValues.isEmpty()) {
            if (random.nextBoolean()) {
                return String.valueOf(randomValue);
            } else {
                return String.valueOf(specifiedValues.get(random.nextInt(specifiedValues.size())));
            }
        } else {
            return String.valueOf(randomValue);
        }
    }

    private String generateRandomIntegerValueFromList(Random random, List<Integer> specifiedValues) {
        if (!specifiedValues.isEmpty()) {
            return String.valueOf(specifiedValues.get(random.nextInt(specifiedValues.size())));
        } else {
            return ""; // Handle case when the list is empty
        }
    }

    private String generateRandomIntegerValueFromMultipleRanges(Random random, List<Pair<Integer, Integer>> ranges) {
        if (!ranges.isEmpty()) {
            Pair<Integer, Integer> range = ranges.get(random.nextInt(ranges.size()));
            int start = range.getFirst();
            int end = range.getSecond();
            if (start <= end) { // Check if start is less than or equal to end
                return String.valueOf(random.nextInt(end - start + 1) + start);
            } else {
                return String.valueOf(random.nextInt(start - end + 1) + end);
            }
        } else {
            return ""; // Handle case when the list of ranges is empty
        }
    }


    private String generateRandomIntegerValueFromMultipleRangesPlusSpecified(Random random, List<Pair<Integer, Integer>> ranges, List<Integer> specifiedValues) {
        if (!ranges.isEmpty()) {
            Pair<Integer, Integer> range = ranges.get(random.nextInt(ranges.size()));
            int diff = Math.abs(range.getFirst() - range.getSecond());
            int randomValue = random.nextInt(diff + 1) + Math.min(range.getFirst(), range.getSecond());
            if (!specifiedValues.isEmpty()) {
                if (random.nextBoolean()) {
                    return String.valueOf(randomValue);
                } else {
                    return String.valueOf(specifiedValues.get(random.nextInt(specifiedValues.size())));
                }
            } else {
                return String.valueOf(randomValue);
            }
        } else {
            return ""; // Handle case when the list of ranges is empty
        }
    }



    public String generateStringAttribute(Attribute attribute, Random random , List<String> specifiedStringValues , String pattern) {
        switch (attribute.getStringGenerationMethod()) {
            case "Random List":
                return generateStringFromList( random , specifiedStringValues);
            case "Pattern":
                return generateStringFromPattern( random , pattern  );

            case "Mixed":
                return generateMixedString(random, specifiedStringValues , pattern );
            default:
                return "";
        }
    }

    private String generateStringFromList(Random random, List<String> specifiedStringValues) {
        if (!specifiedStringValues.isEmpty()) {
            return specifiedStringValues.get(random.nextInt(specifiedStringValues.size()));
        } else {
            return ""; // Handle case when the list is empty
        }
    }

    private String generateStringFromPattern(Random random, String pattern) {
        return faker.regexify(pattern);
    }

    private String generateMixedString(Random random, List<String> specifiedStringValues, String pattern) {
        // Randomly choose between using specified string values or Java Faker
        boolean useSpecifiedValues = random.nextBoolean();

        if (useSpecifiedValues && !specifiedStringValues.isEmpty()) {
            return generateStringFromList(random, specifiedStringValues);
        } else {
            return faker.regexify(pattern);
        }
    }

    public Map<String, Object> getDataForDevice(Long deviceId, String deviceName) {
        Map<String, Object> deviceData = new HashMap<>();
        List<Map<String, Object>> deviceDataList = new ArrayList<>();
        List<String> columnOrder = new ArrayList<>();
        String tableName = deviceName + "_" + deviceId; // Adjust this based on your table naming convention
        String jdbcUrl = "jdbc:postgresql://localhost:5432/ecommerce";
        String username = "root";
        String password = "root";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Fetch column names and store them in the columnOrder list
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                columnOrder.add(columnName);
            }

            while (resultSet.next()) {
                Map<String, Object> rowData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    rowData.put(columnName, columnValue);
                }
                deviceDataList.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle this exception properly in your application
        }

        deviceData.put("data", deviceDataList);
        deviceData.put("columnOrder", columnOrder);
        return deviceData;
    }

    private String toString(List<AttributeGenerationRequest> attributeGenerationRequests) {
        // Create an ObjectMapper to serialize objects to JSON
        ObjectMapper objectMapper = new ObjectMapper();

        // Create a StringBuilder to build the string representation
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("["); // Start of the JSON array

        // Iterate through each AttributeGenerationRequest object
        for (AttributeGenerationRequest request : attributeGenerationRequests) {
            try {
                // Serialize the object to JSON and append it to the string
                String json = objectMapper.writeValueAsString(request);
                stringBuilder.append(json).append(",");
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // Handle serialization exception
            }
        }

        // Remove the trailing comma if there are elements in the list
        if (!attributeGenerationRequests.isEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        stringBuilder.append("]"); // End of the JSON array

        // Convert the StringBuilder to a String and return it
        return stringBuilder.toString();
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle JSON serialization exception
            return null;
        }
    }
    public List<AttributeGenerationRequest> fromString(String jsonString) {
        // Create an ObjectMapper to deserialize JSON
        ObjectMapper objectMapper = new ObjectMapper();

        // List to store deserialized objects
        List<AttributeGenerationRequest> attributeGenerationRequests = new ArrayList<>();

        try {
            // Deserialize the JSON array into a list of AttributeGenerationRequest objects
            AttributeGenerationRequest[] requestsArray = objectMapper.readValue(jsonString, AttributeGenerationRequest[].class);

            // Convert the array to a list
            for (AttributeGenerationRequest request : requestsArray) {
                attributeGenerationRequests.add(request);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle deserialization exception
        }

        return attributeGenerationRequests;
    }


}
