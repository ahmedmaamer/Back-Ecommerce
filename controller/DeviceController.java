package com.example.iotDataGenerator.controller;


import com.example.iotDataGenerator.dto.DeviceDto;
import com.example.iotDataGenerator.dto.PageData;
import com.example.iotDataGenerator.dto.PageLink;
import com.example.iotDataGenerator.entities.Device;
import com.example.iotDataGenerator.service.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceServiceImpl deviceServiceImpl;

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceServiceImpl.getAllDevices();
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long deviceId) {
        Optional<Device> device = deviceServiceImpl.getDeviceById(deviceId);
        return device.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        Device createdDevice = deviceServiceImpl.createDevice(device);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDevice);
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long deviceId) {
        deviceServiceImpl.deleteDevice(deviceId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/find")
    public PageData<DeviceDto> findDashboards(@RequestBody PageLink pageLink) {
        return deviceServiceImpl.findDevices(pageLink);
    }
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getDeviceSummary() {
        Map<String, Long> summary = deviceServiceImpl.getDeviceSummary();
        return ResponseEntity.ok(summary);
    }


    // Other endpoints for updating device, etc.
}
