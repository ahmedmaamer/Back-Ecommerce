package com.example.iotDataGenerator.mapper;


import com.example.iotDataGenerator.dto.DeviceDto;
import com.example.iotDataGenerator.entities.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface DeviceMapper extends BaseMapper<Device, DeviceDto>  {

    @Mapping(source = "deviceId", target = "deviceId")
    @Mapping(source = "deviceName", target = "deviceName")
    @Mapping(source = "deviceType", target = "deviceType")
    @Mapping(source = "createdAt" , target = "createdAt")
    @Mapping(source = "description", target = "description")
    DeviceDto convertEntityToDto(Device dashboard);
    Device convertDtoToEntity(DeviceDto dashboardDto);

}
