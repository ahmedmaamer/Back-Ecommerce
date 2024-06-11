package com.example.iotDataGenerator.repository;


import com.example.iotDataGenerator.entities.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Page<Device> findAll(Specification<Device> specification, Pageable pageable);
    long countByStatus(String status);
    @Query("SELECT d.deviceName FROM Device d WHERE d.deviceId = :deviceId")
    String getDeviceNameByDeviceId(@Param("deviceId") Long deviceId);

}
