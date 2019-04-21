package com.gmail.rebel249.servicemodule.converter;

import com.gmail.rebel249.datamodule.model.Item;
import com.gmail.rebel249.servicemodule.model.ItemDTO;

public interface ItemConverter {

    ItemDTO toItemDTO(Item item);
    Item fromItemDTO(ItemDTO itemDTO);
}
