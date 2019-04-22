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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Stream;

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
    public void createDatabaseTables() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                String databaseCreatorFile = "sqlInitFile.sql";
                String[] databaseInitFile = getQueries(databaseCreatorFile);
                for (String databaseInitialQuery : databaseInitFile) {
                    statement.addBatch(databaseInitialQuery);
                }
                statement.executeBatch();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new IllegalFormatStatementException("Cannot create statement");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionStateException("Cannot create connection using properties");
        }
    }

    private String[] getQueries(String initialFileName) {
        try (Stream<String> fileStream = Files.lines(Paths.get(initialFileName))) {
            return fileStream.reduce(String::concat).orElse("").split(";");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionStateException("Cannot create connection");
        }
    }
}
