package com.financeTracker.mapper;

import com.financeTracker.dto.*;
import org.mapstruct.Mapper;

import java.util.Map;

@Mapper
public interface DtoStatisticsMapper {

    BalanceResponseDto toBalanceResponseDto(Double balance);

    IncomeExpenseResponseDto toIncomeExpenseResponseDto(Double amount);

    GoalStatisticsResponseDto toGoalStatisticsResponseDto(Long completedGoals, Double averageGoalProgress);

    ReportResponseDto toReportResponseDto(
            Double currentBalance, String budgetState, String goalState,
            Double monthlyExpenses, Double monthlyIncome,
            Double totalExpenses, Double totalIncome,
            Long completedGoals, Double averageGoalProgress
    );

    default ExpenseAnalysisResponseDto toExpenseAnalysisResponseDto(Map<String, Double> categoryExpenses) {
        ExpenseAnalysisResponseDto dto = new ExpenseAnalysisResponseDto();
        dto.setCategoryExpenses(categoryExpenses);
        return dto;
    }
}


