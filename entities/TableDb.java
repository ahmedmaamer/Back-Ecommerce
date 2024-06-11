package com.example.iotDataGenerator.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TableDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tableName;
    private String tableType;
    private String schemaName;

    @ManyToOne
    @JoinColumn(name = "database_info_id")
    @JsonIgnore
    private DatabaseInfo databaseInfo;

    @OneToMany(mappedBy = "tableDb", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference

    private List<ColumnDb> columns;

    @OneToMany(mappedBy = "tableDb", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore

    private List<PrimaryKey> primaryKeys;

    @OneToMany(mappedBy = "tableDb", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference

    private List<ForeignKey> foreignKeys;

    @OneToMany(mappedBy = "tableDb", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore

    private List<Index> indexes;
}
