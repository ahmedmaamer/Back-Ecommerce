package com.example.iotDataGenerator.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "attributes")
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attributeId;

    private String attributeName;
    @Transient
    private String attributeValue ;
    private String dataType;
    private String unit;
    private String description;
    @Transient
    private String generationMethod; // Method for generating attribute data
    @Transient
    private String stringGenerationMethod ;
    @Transient
    private String dateGenerationMethod ;

    @ManyToOne
    @JoinColumn(name = "device_id")
    @JsonIgnore
    private Device device;

    // Getters and setters
}
