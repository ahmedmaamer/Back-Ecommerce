package com.example.iotDataGenerator.repository;


import com.example.iotDataGenerator.entities.ForeignKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForeignKeyRepository extends JpaRepository<ForeignKey, Long> {
    void deleteByTableDbId(Long tableDbId);
}