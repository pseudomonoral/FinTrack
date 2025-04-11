package com.audit.model;

import java.time.LocalDateTime;

public record AuditLog(
        String userLogin,
        String eventType,
        String ipAddress,
        LocalDateTime createdAt
) {}