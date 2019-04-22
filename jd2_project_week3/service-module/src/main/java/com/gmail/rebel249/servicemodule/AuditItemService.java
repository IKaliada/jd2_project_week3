package com.gmail.rebel249.servicemodule;

import org.springframework.stereotype.Component;

@Component
public interface AuditItemService {
    void getAdviseDuringAddItem(String action, Long id);
    void getAdviseDuringUpdateItem(String action, Long id);
}
