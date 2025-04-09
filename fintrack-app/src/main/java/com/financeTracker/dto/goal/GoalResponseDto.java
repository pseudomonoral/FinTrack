package com.financeTracker.dto.goal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponseDto {
    private Long id;
    private String name;
    private double targetAmount;
    private double currentAmount;
    private boolean goalActual;
    private double progress;
}

