package com.example.iotDataGenerator.repository;

import com.example.iotDataGenerator.entities.TableDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<TableDb, Long> {

    void deleteByTableName(String tableName);
    @Modifying
    @Transactional
    @Query("DELETE FROM TableDb t WHERE t.tableName IN ?1")
    void deleteByTableNameIn(List<String> tableNames);
}
