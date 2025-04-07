package com.financeTracker.model.entity;

import com.financeTracker.model.type.TransactionType;
import lombok.*;

import java.time.LocalDateTime;


/**
 * Represents a financial transaction.
 */
@Data
@Builder
public class Transaction {

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

    /** Identifier of the user who made the transaction. */
    private Long userId;

    /** Transaction amount. */
    private double amount;

    /** Category of the transaction (e.g., "Food", "Entertainment"). */
    private String category;

    /** Description of the transaction. */
    private String description;

    /** Type of the transaction (e.g., INCOME, EXPENSE). */
    private TransactionType type;
}
