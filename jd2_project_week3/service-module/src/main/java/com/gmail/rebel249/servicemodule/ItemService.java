package com.gmail.rebel249.servicemodule;


import com.gmail.rebel249.servicemodule.model.ItemDTO;

import java.util.List;

public interface ItemService {
    ItemDTO add(ItemDTO itemDTO);
    List<ItemDTO> getItems();
    int update(Long id,String status);
}
