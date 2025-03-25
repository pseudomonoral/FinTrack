package com.financeTracker.mapper;

import com.financeTracker.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface DtoStatisticsMapper {
    DtoStatisticsMapper INSTANCE = Mappers.getMapper(DtoStatisticsMapper.class);

    BalanceResponseDto toBalanceResponseDto(double balance);
    IncomeExpenseResponseDto toIncomeExpenseResponseDto(double amount);
    ExpenseAnalysisResponseDto toExpenseAnalysisResponseDto(Map<String, Double> categoryExpenses);
    GoalStatisticsResponseDto toGoalStatisticsResponseDto(Long completedGoals, Double averageGoalProgress);
    ReportResponseDto toReportResponseDto(double currentBalance, String budgetState, String goalState,
                                          double monthlyExpenses, double monthlyIncome,
                                          double totalExpenses, double totalIncome,
                                          Long completedGoals, Double averageGoalProgress);
}
