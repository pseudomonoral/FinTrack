package com.financeTracker.model.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a user's budget.
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Budget extends BaseEntity {

    /** Identifier of the user associated with the budget. */
    private Long userId;

    /** Spending limit for the budget. */
    private double limit;
}
