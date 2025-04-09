package com.financeTracker.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalStatisticsResponseDto {
    private Long completedGoals;
    private Double averageGoalProgress;
}
