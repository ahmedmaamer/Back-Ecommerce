package com.example.iotDataGenerator.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ColumnDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String columnName;
    private String dataType;
    private int columnSize;
    private boolean nullable;
    private String defaultValue;
    private boolean autoIncrement;

    @ManyToOne
    @JoinColumn(name = "table_db_id")
    @JsonBackReference

    private TableDb tableDb;

}
