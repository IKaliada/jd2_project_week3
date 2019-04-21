package com.gmail.rebel249.datamodule.connection.impl;

import com.gmail.rebel249.datamodule.config.DatabaseProperties;
import com.gmail.rebel249.datamodule.connection.ConnectionService;
import com.gmail.rebel249.datamodule.exception.ConnectionStateException;
import com.gmail.rebel249.datamodule.exception.IllegalDatabaseDriverException;
import com.gmail.rebel249.datamodule.exception.IllegalFormatStatementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Component
public class ConnectionServiceImpl implements ConnectionService {

    private final DatabaseProperties databaseProperties;
    private final static Logger logger = LoggerFactory.getLogger(ConnectionServiceImpl.class);

    @Autowired
    public ConnectionServiceImpl(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
        try {
            Class.forName(databaseProperties.getDriver());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalDatabaseDriverException("Driver not found " + databaseProperties.getDriver());
        }
    }

    @Override
    public Connection getConnection() {
        try {
            Properties properties = new Properties();
            properties.setProperty("user", databaseProperties.getUsername());
            properties.setProperty("password", databaseProperties.getPassword());
            properties.setProperty("useUnicode", "true");
            properties.setProperty("characterEncoding", "cp1251");
            return DriverManager.getConnection(databaseProperties.getUrl(), properties);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionStateException("Cannot create connection using properties");
        }
    }

    @PostConstruct
    private void createDatabaseTables() {
        try (Connection connection = getConnection()) {
            String itemTableQuery = "CREATE TABLE IF NOT EXISTS item (" +
                    "id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(40) NOT NULL, " +
                    "item_status VARCHAR (30) NOT NULL)";
            String auditItemTableQuery = "CREATE TABLE IF NOT EXISTS audit_item (" +
                    "id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    "date DATETIME, " +
                    "action VARCHAR (30) NOT NULL)";
            String alterTable = "ALTER TABLE audit_item ADD item_id BIGINT UNSIGNED NOT NULL, ADD FOREIGN KEY (item_id) REFERENCES item (id);";
            try (Statement statement = connection.createStatement()) {
                statement.execute(itemTableQuery);
                statement.execute(auditItemTableQuery);
                statement.execute(alterTable);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new IllegalFormatStatementException("Cannot create statement");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionStateException("Cannot create connection using properties");
        }
    }
}
