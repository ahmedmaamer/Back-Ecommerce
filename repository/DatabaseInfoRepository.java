package com.example.iotDataGenerator.repository;

import com.example.iotDataGenerator.entities.DatabaseInfo;
import com.example.iotDataGenerator.entities.TableDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DatabaseInfoRepository extends JpaRepository<DatabaseInfo, Long> {
    Optional<DatabaseInfo> findByUsernameAndDatabaseName(String username, String databaseName);

}