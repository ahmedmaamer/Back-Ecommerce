package com.example.iotDataGenerator.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class DatabaseCredentials {
@Id
private Long Id;
    private String url;
    private String username;
    private String password;

    // Getters and Setters
}

