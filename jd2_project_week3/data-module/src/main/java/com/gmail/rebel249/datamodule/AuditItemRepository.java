package com.gmail.rebel249.datamodule;

public interface AuditItemRepository {
    void getAdviseDuringAddingItem(String action, Long id);
    void getAdviseDuringUpdatingItem(String action, Long id);
}
