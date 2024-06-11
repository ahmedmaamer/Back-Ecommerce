package com.example.iotDataGenerator.controller;
import com.example.iotDataGenerator.entities.TableDb;
import com.example.iotDataGenerator.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/tableDb")
public class TableController {

    @Autowired
    private TableService tableService;

    @GetMapping
    public List<TableDb> getAllRecords() {
        return tableService.getAllRecords();
    }

}
