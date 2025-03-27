package com.financeTracker.model.entity;


import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a user's budget.
 */
@Data
@Builder
public class Budget {

    /** Identifier of the user associated with the budget. */
    private Long userId;

    /** Spending limit for the budget. */
    private double limit;
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
