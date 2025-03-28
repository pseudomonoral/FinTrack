package com.financeTracker.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Abstract base entity class that provides common fields for all entities.
 */
@Data
@SuperBuilder
public abstract class BaseEntity {

    /** Unique identifier of the entity. */
    private Long id;

    /** Timestamp when the entity was created. */
    private LocalDateTime createdAt;

    /** Timestamp when the entity was last updated. */
    private LocalDateTime updatedAt;

    /**
     * Initializes timestamps when the entity is created.
     */
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the timestamp when the entity is modified.
     */
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
