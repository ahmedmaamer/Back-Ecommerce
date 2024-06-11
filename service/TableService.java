package com.example.iotDataGenerator.service;

import com.example.iotDataGenerator.entities.TableDb;
import com.example.iotDataGenerator.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;
    public List<TableDb> getAllRecords() {
        return tableRepository.findAll();

    }

    public void deleteByTableName(String tableName) {
        tableRepository.deleteByTableName(tableName);
    }
  }
