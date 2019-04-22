package com.gmail.rebel249.datamodule.impl;

import com.gmail.rebel249.datamodule.AuditItemRepository;
import com.gmail.rebel249.datamodule.connection.ConnectionService;
import com.gmail.rebel249.datamodule.exception.ConnectionStateException;
import com.gmail.rebel249.datamodule.exception.IllegalFormatStatementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class AuditItemRepositoryImpl implements AuditItemRepository {
    private static final Logger logger = LoggerFactory.getLogger(AuditItemRepositoryImpl.class);
    private ConnectionService connectionService;

    @Autowired
    public AuditItemRepositoryImpl(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public void getAdviseDuringAddingItem(String action, Long id) {
        try (Connection connection = connectionService.getConnection()) {
            String aspectAddQuery = "INSERT INTO audit_item (date, action, item_id) VALUES (now(), ?, ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(aspectAddQuery);
                preparedStatement.setString(1, action);
                preparedStatement.setLong(2, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new IllegalFormatStatementException("Cannot execute SQL query " + aspectAddQuery);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionStateException("Cannot create connection");
        }
    }

    @Override
    public void getAdviseDuringUpdatingItem(String action, Long id) {
        try (Connection connection = connectionService.getConnection()) {
            String aspectUpdateQuery = "INSERT INTO audit_item (date, action, item_id) VALUES (now(), ?, ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(aspectUpdateQuery);
                preparedStatement.setString(1, action);
                preparedStatement.setLong(2, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new IllegalFormatStatementException("Cannot execute SQL query " + aspectUpdateQuery);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionStateException("Cannot create connection");
        }
    }
}
