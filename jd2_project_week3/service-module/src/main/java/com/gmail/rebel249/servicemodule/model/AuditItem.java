package com.gmail.rebel249.servicemodule.model;

import java.sql.Date;

public class AuditItem {
    private Long id;
    private ItemAction actionList;
    private Date date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemAction getActionList() {
        return actionList;
    }

    public void setActionList(ItemAction actionList) {
        this.actionList = actionList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
