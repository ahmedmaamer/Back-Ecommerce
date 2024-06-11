package com.example.iotDataGenerator.service;
import com.example.iotDataGenerator.entities.Attribute;
import com.example.iotDataGenerator.entities.Device;
import com.example.iotDataGenerator.repository.AttributeRepository;
import com.example.iotDataGenerator.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private DeviceRepository deviceRepository ;

    public List<Attribute> getAllAttributes() {
        return attributeRepository.findAll();
    }

        public List<Attribute> getAttributesByDeviceId(Long deviceId) {
            return attributeRepository.findByDevice_deviceId(deviceId);
        }


    public Optional<Attribute> getAttributeById(Long attributeId) {
        return attributeRepository.findById(attributeId);
    }

    public Attribute createAttribute(Attribute attribute, Long deviceId) {

        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            attribute.setDevice(device);
            System.out.println("device : " + device.getDeviceName());


            return attributeRepository.save(attribute);
        } else {
            throw new RuntimeException("Device not found with ID: " + deviceId);
        }
    }


    public void deleteAttribute(Long attributeId) {
        attributeRepository.deleteById(attributeId);
    }

    public List<Attribute> updateAttributesByDeviceId(Long deviceId, List<Attribute> updatedAttributes) {
        // Retrieve the device by deviceId
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);

        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();

            // Retrieve all attributes associated with the device
            List<Attribute> existingAttributes = attributeRepository.findByDevice_deviceId(deviceId);

            // Iterate through existing attributes
            for (Attribute existingAttribute : existingAttributes) {
                // Find the corresponding updated attribute
                for (Attribute updatedAttribute : updatedAttributes) {
                    if (existingAttribute.getAttributeId().equals(updatedAttribute.getAttributeId())) {
                        // Update the attribute's properties
                        existingAttribute.setAttributeName(updatedAttribute.getAttributeName());
                        existingAttribute.setDataType(updatedAttribute.getDataType());
                        // You can update other properties similarly
                        // Save the updated attribute
                        attributeRepository.save(existingAttribute);
                        break; // Move to the next existing attribute
                    }
                }
            }

            // Return the updated list of attributes
            return existingAttributes;
        } else {
            // If device with the given ID is not found
            throw new RuntimeException("Device not found with ID: " + deviceId);
        }
    }


    // Other methods...
}



