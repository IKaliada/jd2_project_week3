package com.gmail.rebel249.servicemodule.converter.impl;

import com.gmail.rebel249.datamodule.model.Item;
import com.gmail.rebel249.servicemodule.converter.ItemConverter;
import com.gmail.rebel249.servicemodule.model.ItemDTO;
import org.springframework.stereotype.Component;

@Component
public class ItemConverterImpl implements ItemConverter {
    @Override
    public ItemDTO toItemDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setItem_status(item.getStatus());
        return itemDTO;
    }

    @Override
    public Item fromItemDTO(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setName(itemDTO.getName());
        item.setStatus(itemDTO.getItem_status());
        return item;
    }
}
