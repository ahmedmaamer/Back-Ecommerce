package com.example.iotDataGenerator.repository;

import com.example.iotDataGenerator.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.iotDataGenerator.entities.Attribute;

import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    List<Attribute> findByDevice_deviceId(Long deviceId);

    Attribute findByDevice_deviceIdAndAttributeName(Long deviceId, String attributeName);
    List<Attribute> findByDevice(Device device);
}
