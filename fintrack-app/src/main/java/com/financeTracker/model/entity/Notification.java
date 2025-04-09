package com.financeTracker.model.entity;

import com.financeTracker.model.type.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents a notification entity in the system.
 * This class contains the details of a notification, including its unique identifier,
 * creation timestamp, associated user, type, and read status.
 */
@Data
@Builder
public class Notification {

    /** Unique identifier of the notification entity. */
    private Long id;

    /** Timestamp when the notification was created. */
    private LocalDateTime createdAt;

    /**
     * Initializes the creation timestamp for the notification.
     * This method should be called when the notification entity is first created,
     * setting the {@code createdAt} field to the current date and time.
     */
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /** Identifier of the user associated with the notification. */
    private Long userId;

    /** The type of the notification, represented by {@link NotificationType}. */
    private NotificationType type;

    /** Indicates whether the notification has been read. Defaults to {@code false}. */
    @Builder.Default
    private boolean read = false;
}