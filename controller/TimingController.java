    package com.example.iotDataGenerator.controller;
    import com.example.iotDataGenerator.entities.Device;
    import com.example.iotDataGenerator.service.DeviceService;
    import com.example.iotDataGenerator.entities.TimingData;
    import io.micrometer.core.instrument.Meter;
    import io.micrometer.core.instrument.MeterRegistry;
    import io.micrometer.core.instrument.Timer;
    import io.micrometer.core.instrument.distribution.ValueAtPercentile;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;

    import java.util.*;
    import java.util.concurrent.TimeUnit;

    @CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    @RestController
    public class TimingController {

        @Autowired
        private MeterRegistry meterRegistry;

        @Autowired
        private DeviceService deviceService;

        @GetMapping("/timing")
        public Map<String, TimingData> getTimingData() {
            Map<String, TimingData> timingDataMap = new HashMap<>();
            for (Meter meter : meterRegistry.getMeters()) {
                if (meter instanceof Timer && meter.getId().getTag("device_id") != null) {
                    Timer timer = (Timer) meter;

                    String deviceName = deviceService.getDeviceNameByDeviceId(Long.parseLong(timer.getId().getTag("device_id")));
                    timingDataMap.put(deviceName, extractTimingData(timer));
                }
            }
            return timingDataMap;
        }

        private TimingData extractTimingData(Timer timer) {
            Optional<Device> device=deviceService.getDeviceById(Long.parseLong(timer.getId().getTag("device_id"))) ;

            long count = timer.count() * device.get().getMawDataPoints();
            double totalTimeDouble = timer.totalTime(TimeUnit.MILLISECONDS); // Get total time in milliseconds as double
            long totalTimeMillis = (long) totalTimeDouble; // Cast to long
            ValueAtPercentile[] percentiles = timer.takeSnapshot().percentileValues();
            List<Double> percentileValues = new ArrayList<>();

            for (ValueAtPercentile percentile : percentiles) {
                percentileValues.add(percentile.value());
            }
            return new TimingData(count, totalTimeMillis, percentileValues);
        }
    }
