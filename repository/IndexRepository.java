package com.example.iotDataGenerator.repository;

import com.example.iotDataGenerator.entities.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexRepository extends JpaRepository<Index, Long> {

}
