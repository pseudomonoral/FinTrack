package com.financeTracker.mapper;

import com.financeTracker.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface DtoStatisticsMapper {
    DtoStatisticsMapper INSTANCE = Mappers.getMapper(DtoStatisticsMapper.class);

    @Mapping(target = "balance", source = "balance")
    BalanceResponseDto toBalanceResponseDto(Double balance);

    @Mapping(target = "amount", source = "amount")
    IncomeExpenseResponseDto toIncomeExpenseResponseDto(Double amount);

    @Mapping(target = "categoryExpenses", source = "categoryExpenses")
    ExpenseAnalysisResponseDto toExpenseAnalysisResponseDto(Map<String, Double> categoryExpenses);

    @Mapping(target = "completedGoals", source = "completedGoals")
    @Mapping(target = "averageGoalProgress", source = "averageGoalProgress")
    GoalStatisticsResponseDto toGoalStatisticsResponseDto(Long completedGoals, Double averageGoalProgress);

    @Mappings({
            @Mapping(target = "currentBalance", source = "currentBalance"),
            @Mapping(target = "budgetState", source = "budgetState"),
            @Mapping(target = "goalState", source = "goalState"),
            @Mapping(target = "monthlyExpenses", source = "monthlyExpenses"),
            @Mapping(target = "monthlyIncome", source = "monthlyIncome"),
            @Mapping(target = "totalExpenses", source = "totalExpenses"),
            @Mapping(target = "totalIncome", source = "totalIncome"),
            @Mapping(target = "completedGoals", source = "completedGoals"),
            @Mapping(target = "averageGoalProgress", source = "averageGoalProgress")
    })
    ReportResponseDto toReportResponseDto(
            Double currentBalance, String budgetState, String goalState,
            Double monthlyExpenses, Double monthlyIncome,
            Double totalExpenses, Double totalIncome,
            Long completedGoals, Double averageGoalProgress
    );
}

