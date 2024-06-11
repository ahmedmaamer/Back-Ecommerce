package com.example.iotDataGenerator.service;


import com.example.iotDataGenerator.dto.DeviceDto;
import com.example.iotDataGenerator.dto.FilterModel;
import com.example.iotDataGenerator.dto.PageData;
import com.example.iotDataGenerator.dto.PageLink;
import com.example.iotDataGenerator.entities.Attribute;
import com.example.iotDataGenerator.entities.Device;
import com.example.iotDataGenerator.mapper.DeviceMapper;
import com.example.iotDataGenerator.repository.AttributeRepository;
import com.example.iotDataGenerator.repository.DeviceRepository;
import com.example.iotDataGenerator.utils.FilterUtil;
import com.example.iotDataGenerator.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.*;


@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private  DeviceMapper deviceMapper;

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private AttributeRepository attributeRepository ;
    @PersistenceContext
    private EntityManager entityManager;



    @Override
    public PageData<DeviceDto> findDevices(PageLink pageLink) {
        Map<String, List<FilterModel>> filters =
                pageLink.getFilters() != null ? pageLink.getFilters() : Collections.emptyMap();
        Pageable pageable = PaginationUtil.toPageable(pageLink);
        Specification<Device> specification = FilterUtil.filter(filters,
                pageLink.getGlobalFilter());
        return fetchDevicesData(specification, pageable);
    }

    private PageData<DeviceDto> fetchDevicesData(Specification<Device> specification,
                                                       Pageable pageable) {
        Page<Device> dashboards = deviceRepository.findAll(specification, pageable);
        return PaginationUtil.paginate(dashboards, deviceMapper);
    }


    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Optional<Device> getDeviceById(Long deviceId) {
        return deviceRepository.findById(deviceId);
    }

    public Device createDevice(Device device) {
            device.setStatus("inactive");

        return deviceRepository.save(device);
    }

    @Transactional
    public void deleteDevice(Long deviceId) {
        // Fetch the device by ID
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();

            // Fetch all attributes associated with the device
            List<Attribute> attributes = attributeRepository.findByDevice(device);

            // Delete attributes associated with the device
            for (Attribute attribute : attributes) {
                attributeRepository.delete(attribute);
            }

            // Delete the device
            deviceRepository.deleteById(deviceId);

            // Drop the table with the same name as the device
            // Sanitize table name
            String tableName = (device.getDeviceName() + "_" + deviceId).replaceAll("[^a-zA-Z0-9_]", "_");

            if (!tableName.isEmpty()) {
                entityManager.createNativeQuery("DROP TABLE IF EXISTS " + tableName).executeUpdate();
            }

        } else {
            // Handle case when device is not found
            throw new EntityNotFoundException("Device not found with ID: " + deviceId);
        }
    }
    public Map<String, Long> getDeviceSummary() {
        Map<String, Long> summary = new HashMap<>();
        summary.put("totalDevices", deviceRepository.count());
        summary.put("activeDevices", deviceRepository.countByStatus("active"));
        summary.put("inactiveDevices", deviceRepository.countByStatus("inactive"));
        return summary;
    }
    @Override
    public String getDeviceNameByDeviceId(Long deviceId) {
        return deviceRepository.getDeviceNameByDeviceId(deviceId);
    }
}
