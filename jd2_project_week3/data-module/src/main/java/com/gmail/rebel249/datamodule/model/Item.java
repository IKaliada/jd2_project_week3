package com.gmail.rebel249.datamodule.model;

public class Item {
    private Long id;
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

    public String getStatus() {
        return item_status;
    }

    public void setStatus(String item_status) {
        this.item_status = item_status;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", item_status=" + item_status +
                '}';
    }
}
