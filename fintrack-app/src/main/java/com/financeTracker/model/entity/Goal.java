package com.financeTracker.model.entity;



import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents a financial goal for a user.
 */
@Data
@Builder
public class Goal{

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

    /** Identifier of the user who owns the goal. */
    private Long userId;

    /** Name of the goal. */
    private String name;

    /** Target amount to achieve the goal. */
    private double targetAmount;

    /** Current saved amount towards the goal. Default is 0.0. */
    @Builder.Default
    private double currentAmount = 0.0;

    /** Flag indicating whether the goal has been completed. Default is false. */
    @Builder.Default
    private boolean goalCompleted = false;

    /** Flag indicating whether the goal is currently active. Default is false. */
    @Builder.Default
    private boolean goalActual = false;
}