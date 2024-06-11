package com.example.iotDataGenerator.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry {

    private LocalDateTime dateTime;
    private String thread;
    private String level;
    private String message;


}
