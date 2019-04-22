package com.gmail.rebel249.servicemodule.impl;

import com.gmail.rebel249.datamodule.AuditItemRepository;
import com.gmail.rebel249.datamodule.connection.ConnectionService;
import com.gmail.rebel249.datamodule.exception.ConnectionStateException;
import com.gmail.rebel249.servicemodule.AuditItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class AuditItemServiceImpl implements AuditItemService {
    private static final Logger logger = LoggerFactory.getLogger(AuditItemServiceImpl.class);
    private final ConnectionService connectionService;
    private final AuditItemRepository auditItemRepository;

    @Autowired
    public AuditItemServiceImpl(ConnectionService connectionService, AuditItemRepository auditItemRepository) {
        this.connectionService = connectionService;
        this.auditItemRepository = auditItemRepository;
    }

    @Override
    public void getAdviseDuringAddItem(String action, Long id) {
        try (Connection connection = connectionService.getConnection()) {
            try {
                connection.setAutoCommit(false);
                auditItemRepository.getAdviseDuringAddingItem(action, id);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage());
                throw new ConnectionStateException("Cannot create connection");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new ConnectionStateException("Cannot create connection");
        }
    }

    @Override
    public void getAdviseDuringUpdateItem(String action, Long id) {
        try (Connection connection = connectionService.getConnection()) {
            try {
                connection.setAutoCommit(false);
                auditItemRepository.getAdviseDuringUpdatingItem(action, id);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage());
                throw new ConnectionStateException("Cannot create connection");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new ConnectionStateException("Cannot create connection");
        }
    }
}
