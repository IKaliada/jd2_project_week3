package com.gmail.rebel249.servicemodule;

import com.gmail.rebel249.servicemodule.model.AuditItem;
import org.springframework.stereotype.Component;

@Component
public interface AuditItemService {
    AuditItem save(AuditItem auditItem);
}
