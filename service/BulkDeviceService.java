package com.example.iotDataGenerator.service;

import com.example.iotDataGenerator.enums.Topic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
@Service
public class BulkDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(BulkDeviceService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Faker faker = new Faker();



    public int processCSVData(InputStream inputStream, int numRowsPerRow) {
        int numRowsGenerated = 0; // Initialize the counter for the number of rows generated

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            // Skip the header row

            // Process each data row
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 2) {
                    System.err.println("Invalid row: " + line);
                    continue;
                }

                String deviceName = values[0];
                String deviceType = values[1];

                for (int j = 0; j < numRowsPerRow; j++) {
                    Map<String, String> deviceData = new HashMap<>();
                    for (int i = 2; i < values.length; i++) {
                        String attributeName = values[i];
                        String attributeValue = String.valueOf(faker.random().nextLong());
                        deviceData.put(attributeName, attributeValue);
                    }

                    // Add deviceName and deviceType to the deviceData map
                    deviceData.put("deviceName", deviceName);
                    deviceData.put("deviceType", deviceType);

                    // Convert deviceData map to JSON string
                    String jsonData = objectMapper.writeValueAsString(deviceData);

                    // Publish JSON string to Kafka topic
                    kafkaTemplate.send(Topic.LIMITTEST.getTopic(), jsonData);
                    logger.info("sent " , jsonData);
                    System.out.println(jsonData);
                    numRowsGenerated++; // Increment the counter for each row generated

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numRowsGenerated; // Return the total number of rows generated

    }
}
