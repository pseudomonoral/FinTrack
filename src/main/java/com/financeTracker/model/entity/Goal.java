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

    boolean goalCompleted;

    public Goal(Long id, boolean goalCompleted, Long userId, String name, double targetAmount) {
        super(id);
        this.goalCompleted = goalCompleted;
        this.userId = userId;
        this.name = name;
        this.targetAmount = targetAmount;
    }
}