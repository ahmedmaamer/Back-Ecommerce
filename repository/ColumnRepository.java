package com.example.iotDataGenerator.repository;

import com.example.iotDataGenerator.entities.ColumnDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnRepository extends JpaRepository<ColumnDb, Long> {
    void deleteByTableDbId(Long tableDbId);

}
