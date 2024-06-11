package com.example.iotDataGenerator.repository;


import com.example.iotDataGenerator.entities.Device;
import com.example.iotDataGenerator.entities.DeviceData;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface DeviceDataRepository extends JpaRepository<DeviceData, Long> {
    boolean existsByDeviceAndAttributeName(Device device, String attributeName);
    Optional<DeviceData> findFirstByDeviceAndAttributeNameOrderByTimestampDesc(Device device, String attributeName);



}
