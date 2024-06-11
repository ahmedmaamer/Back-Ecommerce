package com.example.iotDataGenerator.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@AllArgsConstructor
@Entity
public class DatabaseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String productVersion;
    private String driverName;
    private String driverVersion;
    private String databaseName;  // New field
    private String username;  // New field
    @OneToMany(mappedBy = "databaseInfo", cascade = CascadeType.ALL)
    @JsonIgnore

    private List<TableDb> tables;
    public DatabaseInfo() {
        this.tables = new ArrayList<>();
    }
}
