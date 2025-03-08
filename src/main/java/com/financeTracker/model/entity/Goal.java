package com.financeTracker.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Goal extends BaseEntity {

    Long userId;

    String name;

    double targetAmount;

    @Builder.Default
    boolean goalCompleted = false;

    public Goal(Long id, Long userId, boolean goalCompleted, String name, double targetAmount) {
        super(id);
        this.goalCompleted = goalCompleted;
        this.userId = userId;
        this.name = name;
        this.targetAmount = targetAmount;
    }
}