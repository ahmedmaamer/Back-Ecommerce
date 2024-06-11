package com.example.iotDataGenerator.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long deviceId;

    private String deviceName;

    @Column(name = "device_type")
    private String deviceType;
    @Column(name = "createdAt")
    private Timestamp createdAt;
    @Column(columnDefinition = "TEXT")
    private String configurationJson;


    private String description;
    private long period ;
    private int cycles ;
    private int mawDataPoints ;
    private String status;


    public int getMawDataPoints() {
        return mawDataPoints;
    }

    public void setMawDataPoints(int mawDataPoints) {
        this.mawDataPoints = mawDataPoints;
    }

    // Add FetchType.LAZY to prevent eager loading of attributes
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attribute> attributes;

    // Constructors, getters, and setters

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
    @Override
    public String toString() {
        return "Device{" +
                "deviceId=" + deviceId +
                ", name='" + deviceName + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}