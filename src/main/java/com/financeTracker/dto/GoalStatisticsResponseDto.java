package com.financeTracker.dto;

import lombok.Data;

@Data
public class GoalStatisticsResponseDto {
    private Long completedGoals;
    private Double averageGoalProgress;
}
