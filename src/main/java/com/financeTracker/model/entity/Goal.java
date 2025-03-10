package com.financeTracker.model.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class Goal extends BaseEntity {

    private Long userId;

    private String name;

    private double targetAmount;

    @Builder.Default
    private double currentAmount = 0L;

    @Builder.Default
    private boolean goalCompleted = false;

    @Builder.Default
    private boolean goalActual = false;

    public Goal(Long id, Long userId, boolean goalCompleted, String name, double targetAmount, boolean goalIsActual) {
        super(id);
        this.goalCompleted = goalCompleted;
        this.userId = userId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.goalActual = goalIsActual;
    }
}