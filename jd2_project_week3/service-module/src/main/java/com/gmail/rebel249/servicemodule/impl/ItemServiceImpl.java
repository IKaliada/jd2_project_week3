package com.gmail.rebel249.servicemodule.impl;

import com.gmail.rebel249.datamodule.ItemRepository;
import com.gmail.rebel249.datamodule.connection.ConnectionService;
import com.gmail.rebel249.datamodule.exception.ConnectionStateException;
import com.gmail.rebel249.datamodule.model.Item;
import com.gmail.rebel249.servicemodule.ItemService;
import com.gmail.rebel249.servicemodule.converter.ItemConverter;
import com.gmail.rebel249.servicemodule.model.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final static Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    private ItemConverter itemConverter;
    private ItemRepository itemRepository;
    private ConnectionService connectionService;

    @Autowired
    public ItemServiceImpl(ItemConverter itemConverter, ItemRepository itemRepository, ConnectionService connectionService) {
        this.itemConverter = itemConverter;
        this.itemRepository = itemRepository;
        this.connectionService = connectionService;
    }

    @Override
    public ItemDTO add(ItemDTO itemDTO) {
        try (Connection connection = connectionService.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Item item = itemRepository.add(itemConverter.fromItemDTO(itemDTO));
                itemDTO = itemConverter.toItemDTO(item);
                connection.commit();
                return itemDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ConnectionStateException("Cannot create connection");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionStateException("Cannot create connection");
        }
    }

    @Override
    public List<ItemDTO> getItems() {
        List<ItemDTO> itemDTOS;
        List<Item> items;
        try (Connection connection = connectionService.getConnection()) {
            try {
                connection.setAutoCommit(false);
                items = itemRepository.getItems();
                itemDTOS = items.stream().map(i -> itemConverter.toItemDTO(i)).collect(Collectors.toList());
                connection.commit();
                return itemDTOS;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ConnectionStateException("Cannot create connection");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionStateException("Cannot create connection");
        }
    }

    @Override
    public int update(Long id, String status) {
        try (Connection connection = connectionService.getConnection()) {
            try {
                connection.setAutoCommit(false);
                int numberOfUpdatedItems = itemRepository.update(id, status);
                connection.commit();
                return numberOfUpdatedItems;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage());
                throw new ConnectionStateException("Cannot create connection");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionStateException("Cannot create connection");
        }
    }
}
