package com.example.iotDataGenerator.service;

import com.example.iotDataGenerator.dto.DeviceDto;
import com.example.iotDataGenerator.dto.PageData;
import com.example.iotDataGenerator.dto.PageLink;
import com.example.iotDataGenerator.entities.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceService {
    List<Device> getAllDevices();
    Optional<Device> getDeviceById(Long deviceId);
    Device createDevice(Device device);
    void deleteDevice(Long deviceId);
    PageData<DeviceDto> findDevices(PageLink  pageLink);
    String getDeviceNameByDeviceId(Long deviceId);

}
