package com.financeTracker.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a financial goal for a user.
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Goal extends BaseEntity {

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