package com.gmail.rebel249.servicemodule.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ItemDTO {
    private Long id;
    @NotNull
    @Size(max = 40)
    private String name;
    private String item_status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", item_status=" + item_status +
                '}';
    }
}
