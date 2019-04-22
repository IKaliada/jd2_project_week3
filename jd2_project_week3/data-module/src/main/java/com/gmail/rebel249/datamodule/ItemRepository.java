package com.gmail.rebel249.datamodule;


import com.gmail.rebel249.datamodule.model.Item;

import java.util.List;

public interface ItemRepository {
    Item add(Item item);
    List<Item> getItems();
    int update(Long id, String status);
}
