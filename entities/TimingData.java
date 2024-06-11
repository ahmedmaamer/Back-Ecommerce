package com.example.iotDataGenerator.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimingData {
    private long count;
    private long totalTimeMillis;
    private List<Double> percentileValues;



}