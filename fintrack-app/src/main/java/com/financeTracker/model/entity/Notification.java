package com.financeTracker.model.entity;

import com.financeTracker.model.type.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Notification {

    /** Unique identifier of the entity. */
    private Long id;

    /** Timestamp when the entity was created. */
    private LocalDateTime createdAt;

    /**
     * Initializes timestamps when the entity is created.
     */
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    private Long userId;

    private NotificationType type;

    @Builder.Default
    private boolean read = false;
}
