package com.financeTracker.model.entity;

import com.financeTracker.model.type.TransactionType;
import lombok.*;
import lombok.experimental.SuperBuilder;


/**
 * Represents a financial transaction.
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Transaction extends BaseEntity {

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
