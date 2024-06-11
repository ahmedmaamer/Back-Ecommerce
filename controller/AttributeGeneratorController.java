package com.example.iotDataGenerator.controller;

import com.example.iotDataGenerator.entities.AttributeGenerationRequest;
import com.example.iotDataGenerator.entities.Device;
import com.example.iotDataGenerator.service.AttributeGeneratorService;
import com.example.iotDataGenerator.service.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/api/generate")
public class AttributeGeneratorController {

    private final AttributeGeneratorService attributeGeneratorService;
    private final DeviceServiceImpl deviceServiceImpl;

    @Autowired
    public AttributeGeneratorController(AttributeGeneratorService attributeGeneratorService, DeviceServiceImpl deviceServiceImpl) {
        this.attributeGeneratorService = attributeGeneratorService;
        this.deviceServiceImpl = deviceServiceImpl;
    }
    @PostMapping("/{deviceId}/generate-data")
    public ResponseEntity<String> generateDataForAttributes(
            @PathVariable Long deviceId,
            @RequestBody List<AttributeGenerationRequest> attributeGenerationRequests,
            @RequestParam(value = "maxDataPoints", required = false, defaultValue = "1") int maxDataPoints,
            @RequestParam long period,
            @RequestParam(value = "cycles", required = true) int cycles

    ) {
        Device device = deviceServiceImpl.getDeviceById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        attributeGeneratorService.scheduleDataGeneration(deviceId, attributeGenerationRequests, maxDataPoints, period,cycles);

        return ResponseEntity.ok("Data generation scheduled successfully for specified attributes of the device");
    }


    @PostMapping("/{deviceId}/generate-data-withou-config")
    public ResponseEntity<Object> generateDataForAttributesWithoutConfig(
            @PathVariable Long deviceId,
            @RequestParam(value = "maxDataPoints", required = false, defaultValue = "1") int maxDataPoints,
            @RequestParam long period,
            @RequestParam(value = "cycles", required = true) int cycles

    )
            {
        Device device = deviceServiceImpl.getDeviceById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));
        attributeGeneratorService.scheduleDataGeneration(deviceId, attributeGeneratorService.fromString(device.getConfigurationJson()), device.getMawDataPoints() , period,cycles);


        // Create a JSON response object
        Map<String, String> response = new HashMap<>();
        response.put("message", "Data generated successfully for specified attributes of the device");

        // Return JSON response with status 200 OK
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{deviceId}/{deviceName}")
    public Map<String, Object> getDataForDevice(@PathVariable Long deviceId, @PathVariable String deviceName) {
        return attributeGeneratorService.getDataForDevice(deviceId, deviceName);
    }
    @GetMapping("/percentage-differences")
    public Map<String, Double> getPercentageDifferences(@RequestParam Long deviceId, @RequestParam String deviceName) {
        return attributeGeneratorService.getPercentageDifferences(deviceId, deviceName);
    }
    @GetMapping("/top3-statistics")
    public Map<String, Map<String, Object>> getTop3GeneratedDataStatistics(@RequestParam Long deviceId, @RequestParam String deviceName) {
        return attributeGeneratorService.getTop3GeneratedDataStatistics(deviceId, deviceName);
    }
    @GetMapping("/average-percentage-difference")
    public double getAveragePercentageDifference(@RequestParam Long deviceId, @RequestParam String deviceName) {
        return attributeGeneratorService.getAveragePercentageDifference(deviceId, deviceName);
    }
}
