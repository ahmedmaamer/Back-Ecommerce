package com.example.iotDataGenerator.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter

public class LogEntryDto {
    private Long id ;
    private LocalDateTime dateTime;
    private String thread;
    private String level;
    private String message;


}
