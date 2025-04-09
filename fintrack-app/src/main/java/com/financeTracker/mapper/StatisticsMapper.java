package com.financeTracker.mapper;

import com.financeTracker.dto.statistic.BalanceResponseDto;
import com.financeTracker.dto.statistic.ExpenseAnalysisResponseDto;
import com.financeTracker.dto.statistic.GoalStatisticsResponseDto;
import com.financeTracker.dto.statistic.IncomeExpenseResponseDto;
import org.mapstruct.Mapper;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface StatisticsMapper {
    BalanceResponseDto toBalanceResponseDto(Double balance);

    IncomeExpenseResponseDto toIncomeExpenseResponseDto(Double amount);

    GoalStatisticsResponseDto toGoalStatisticsResponseDto(Long completedGoals, Double averageGoalProgress);

    default ExpenseAnalysisResponseDto toExpenseAnalysisResponseDto(Map<String, Double> categoryExpenses) {
        return ExpenseAnalysisResponseDto.builder()
                .categoryExpenses(categoryExpenses)
                .build();
    }
}
