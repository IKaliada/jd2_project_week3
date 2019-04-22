package com.gmail.rebel249.datamodule.impl;

import com.gmail.rebel249.datamodule.ItemRepository;
import com.gmail.rebel249.datamodule.connection.ConnectionService;
import com.gmail.rebel249.datamodule.exception.ConnectionStateException;
import com.gmail.rebel249.datamodule.exception.IllegalDatabaseValueException;
import com.gmail.rebel249.datamodule.exception.IllegalFormatStatementException;
import com.gmail.rebel249.datamodule.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private static final Logger logger = LoggerFactory.getLogger(ItemRepositoryImpl.class);

    private ConnectionService connectionService;

    @Autowired
    public ItemRepositoryImpl(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public Item add(Item item) {
        try (Connection connection = connectionService.getConnection()) {
            String itemQuery = "INSERT INTO item (name, item_status) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(itemQuery, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, item.getName());
                preparedStatement.setString(2, "READY");
                preparedStatement.executeUpdate();
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        item.setId(resultSet.getLong(1));
                    }
                }
                return item;
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new IllegalFormatStatementException("Cannot execute SQL query " + itemQuery);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionStateException("Cannot create connection ");
        }
    }

    @Override
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        String itemQuery = "SELECT * FROM item";
        try (PreparedStatement preparedStatement = connectionService
                .getConnection().prepareStatement(itemQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Item item = getItem(resultSet);
                items.add(item);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalFormatStatementException("Cannot execute SQL query " + itemQuery);
        }
        return items;
    }

    @Override
    public int update(Long id, String status) {
        try (Connection connection = connectionService.getConnection()) {
            String updateQuery = "UPDATE item SET item_status = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, status);
                preparedStatement.setLong(2, id);
                preparedStatement.executeUpdate();
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new IllegalFormatStatementException("Cannot execute SQL query " + updateQuery);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new ConnectionStateException("Cannot create connection ");
        }
        return 0;
    }

    private Item getItem(ResultSet resultSet) {
        Item item = new Item();
        try {
            item.setId(resultSet.getLong("id"));
            item.setName(resultSet.getString("name"));
            item.setStatus(resultSet.getString("item_status"));
            return item;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalDatabaseValueException("Database exception during getting items");
        }
    }
}
