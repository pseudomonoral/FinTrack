package com.financeTracker.mapper;

import com.financeTracker.dto.BalanceResponseDto;
import com.financeTracker.dto.ExpenseAnalysisResponseDto;
import com.financeTracker.dto.GoalStatisticsResponseDto;
import com.financeTracker.dto.IncomeExpenseResponseDto;
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
