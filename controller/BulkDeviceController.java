package com.example.iotDataGenerator.controller;

import com.example.iotDataGenerator.service.BulkDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController

public class BulkDeviceController {
    @Autowired
    BulkDeviceService bulkDeviceService ;

    @PostMapping("/process-csv")
    public ResponseEntity<String> processCSV(@RequestParam("file") MultipartFile file, @RequestParam("numRowsPerRow") int numRowsPerRow) {
        try {
            InputStream inputStream = file.getInputStream();
            int numRowsGenerated = bulkDeviceService.processCSVData(inputStream, numRowsPerRow);
            String message = "CSV processing completed. Number of rows generated: " + numRowsGenerated;
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing CSV: " + e.getMessage());
        }
    }


}
