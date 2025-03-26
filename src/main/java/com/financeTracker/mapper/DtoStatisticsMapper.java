package com.financeTracker.mapper;

import com.financeTracker.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface DtoStatisticsMapper {
    DtoStatisticsMapper INSTANCE = Mappers.getMapper(DtoStatisticsMapper.class);

    BalanceResponseDto toBalanceResponseDto(Double balance);

    IncomeExpenseResponseDto toIncomeExpenseResponseDto(Double amount);

    ExpenseAnalysisResponseDto toExpenseAnalysisResponseDto(Map<String, Double> categoryExpenses);

    GoalStatisticsResponseDto toGoalStatisticsResponseDto(Long completedGoals, Double averageGoalProgress);

    ReportResponseDto toReportResponseDto(
            Double currentBalance, String budgetState, String goalState,
            Double monthlyExpenses, Double monthlyIncome,
            Double totalExpenses, Double totalIncome,
            Long completedGoals, Double averageGoalProgress
    );
}

