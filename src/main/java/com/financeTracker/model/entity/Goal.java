package com.financeTracker.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Goal extends BaseEntity {

    private Long userId;

    private String name;

    private double targetAmount;

    @Builder.Default
    private double currentAmount = 0.0;

    @Builder.Default
    private boolean goalCompleted = false;

    @Builder.Default
    private boolean goalActual = false;
}