package com.example.iotDataGenerator.controller;

import com.example.iotDataGenerator.entities.DatabaseCredentials;
import com.example.iotDataGenerator.entities.DatabaseInfo;
import com.example.iotDataGenerator.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/metadata")
public class MetaDataController {

    @Autowired
    private MetadataService metadataService;

    @PostMapping("/extract")
    public ResponseEntity<DatabaseInfo> extractMetadata(@RequestBody DatabaseCredentials credentials) {
        try {
            DatabaseInfo dbInfo = metadataService.extractMetadata(credentials.getUrl(), credentials.getUsername(), credentials.getPassword());
            return ResponseEntity.ok(dbInfo);
        } catch (SQLException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
