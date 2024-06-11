package com.example.iotDataGenerator.service;

import com.example.iotDataGenerator.entities.*;
import com.example.iotDataGenerator.repository.ColumnRepository;
import com.example.iotDataGenerator.repository.DatabaseInfoRepository;
import com.example.iotDataGenerator.repository.ForeignKeyRepository;
import com.example.iotDataGenerator.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class MetadataService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseInfoRepository databaseInfoRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ColumnRepository columnDbRepository;

    @Autowired
    private ForeignKeyRepository foreignKeyRepository;

    private static final Logger logger = Logger.getLogger(MetadataService.class.getName());

    @Transactional
    public DatabaseInfo extractMetadata(String url, String username, String password) throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseName = connection.getCatalog();

            Optional<DatabaseInfo> existingDbInfoOpt = databaseInfoRepository.findByUsernameAndDatabaseName(username, databaseName);
            DatabaseInfo dbInfo = existingDbInfoOpt.orElseGet(DatabaseInfo::new);

            dbInfo.setProductName(metaData.getDatabaseProductName());
            dbInfo.setProductVersion(metaData.getDatabaseProductVersion());
            dbInfo.setDriverName(metaData.getDriverName());
            dbInfo.setDriverVersion(metaData.getDriverVersion());
            dbInfo.setDatabaseName(databaseName);
            dbInfo.setUsername(username);

            // Retrieve the existing list of tables from dbInfo
            List<TableDb> existingTables = dbInfo.getTables() != null ? dbInfo.getTables() : new ArrayList<>();

            // Retrieve the list of tables from the database
            List<String> tablesInDatabase = new ArrayList<>();
            ResultSet tablesResultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (tablesResultSet.next()) {
                String tableName = tablesResultSet.getString("TABLE_NAME");
                tablesInDatabase.add(tableName);

                // Check if the table exists in the existingTables list
                boolean tableExists = existingTables.stream().anyMatch(table -> table.getTableName().equals(tableName));

                if (!tableExists) {
                    // If the table doesn't exist, add it to the existingTables list
                    TableDb tableDb = new TableDb();
                    tableDb.setTableName(tableName);
                    tableDb.setTableType(tablesResultSet.getString("TABLE_TYPE"));
                    tableDb.setSchemaName(tablesResultSet.getString("TABLE_SCHEM"));
                    tableDb.setDatabaseInfo(dbInfo);

                    // Retrieve and set columns, primary keys, foreign keys, and indexes for the table
                    setTableDetails(metaData, tableDb);

                    existingTables.add(tableDb);
                }
            }

            // Identify tables that exist in existingTables but not in the database
            List<String> existingTableNames = existingTables.stream().map(TableDb::getTableName).collect(Collectors.toList());
            List<String> tablesToRemove = existingTableNames.stream()
                    .filter(tableName -> !tablesInDatabase.contains(tableName))
                    .collect(Collectors.toList());

            // Remove identified tables and their associated columns and foreign keys from the database
            for (String tableName : tablesToRemove) {
                Optional<TableDb> tableOpt = existingTables.stream().filter(table -> table.getTableName().equals(tableName)).findFirst();
                if (tableOpt.isPresent()) {
                    TableDb table = tableOpt.get();

                    // Delete associated columns
                    columnDbRepository.deleteByTableDbId(table.getId());

                    // Delete associated foreign keys
                    foreignKeyRepository.deleteByTableDbId(table.getId());

                    // Finally, delete the table itself
                    tableRepository.delete(table);
                }
            }
            existingTables.removeIf(table -> tablesToRemove.contains(table.getTableName()));

            // Set the updated list of tables in dbInfo
            dbInfo.setTables(existingTables);

            return databaseInfoRepository.save(dbInfo);
        }
    }

    private void setTableDetails(DatabaseMetaData metaData, TableDb tableDb) throws SQLException {
        String tableName = tableDb.getTableName();

        // Retrieve columns for the table
        ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, "%");
        List<ColumnDb> columns = new ArrayList<>();
        while (columnsResultSet.next()) {
            ColumnDb column = new ColumnDb();
            column.setColumnName(columnsResultSet.getString("COLUMN_NAME"));
            column.setDataType(columnsResultSet.getString("TYPE_NAME"));
            column.setColumnSize(columnsResultSet.getInt("COLUMN_SIZE"));
            column.setNullable("YES".equals(columnsResultSet.getString("IS_NULLABLE")));
            column.setDefaultValue(columnsResultSet.getString("COLUMN_DEF"));
            column.setAutoIncrement("YES".equals(columnsResultSet.getString("IS_AUTOINCREMENT")));
            column.setTableDb(tableDb);
            columns.add(column);
        }
        tableDb.setColumns(columns);

        // Retrieve primary keys for the table
        ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(null, null, tableName);
        List<PrimaryKey> primaryKeys = new ArrayList<>();
        while (primaryKeyResultSet.next()) {
            PrimaryKey primaryKey = new PrimaryKey();
            primaryKey.setColumnName(primaryKeyResultSet.getString("COLUMN_NAME"));
            primaryKey.setSequence(primaryKeyResultSet.getInt("KEY_SEQ"));
            primaryKey.setTableDb(tableDb);
            primaryKeys.add(primaryKey);
        }
        tableDb.setPrimaryKeys(primaryKeys);

        // Retrieve foreign keys for the table
        ResultSet foreignKeyResultSet = metaData.getImportedKeys(null, null, tableName);
        List<ForeignKey> foreignKeys = new ArrayList<>();
        while (foreignKeyResultSet.next()) {
            ForeignKey foreignKey = new ForeignKey();
            foreignKey.setColumnName(foreignKeyResultSet.getString("FKCOLUMN_NAME"));
            foreignKey.setReferencedTableName(foreignKeyResultSet.getString("PKTABLE_NAME"));
            foreignKey.setReferencedColumnName(foreignKeyResultSet.getString("PKCOLUMN_NAME"));
            foreignKey.setTableDb(tableDb);
            foreignKeys.add(foreignKey);
        }
        tableDb.setForeignKeys(foreignKeys);

        // Retrieve indexes for the table
        ResultSet indexResultSet = metaData.getIndexInfo(null, null, tableName, false, false);
        List<Index> indexes = new ArrayList<>();
        while (indexResultSet.next()) {
            Index index = new Index();
            index.setIndexName(indexResultSet.getString("INDEX_NAME"));
            String columnName = indexResultSet.getString("COLUMN_NAME");
            if (columnName != null) {
                index.setColumnName(columnName);
            } else {
                logger.warning("Null column name for index: " + index.getIndexName());
            }
            index.setUnique(!indexResultSet.getBoolean("NON_UNIQUE"));
            index.setTableDb(tableDb);
            indexes.add(index);
        }
        tableDb.setIndexes(indexes);
    }
}
