package com.example.iotDataGenerator.dto;

import com.example.iotDataGenerator.entities.Attribute;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
public class DeviceDto {
private Long deviceId;
private String deviceName;
private String deviceType;
private Timestamp createdAt;
private String description;
private List<Attribute> attributes;
}