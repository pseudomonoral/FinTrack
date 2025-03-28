package com.financeTracker.mapper;

import com.financeTracker.dto.BalanceResponseDto;
import com.financeTracker.dto.ExpenseAnalysisResponseDto;
import com.financeTracker.dto.GoalStatisticsResponseDto;
import com.financeTracker.dto.IncomeExpenseResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface StatisticsMapper {
    StatisticsMapper INSTANCE = Mappers.getMapper(StatisticsMapper.class);

    BalanceResponseDto toBalanceResponseDto(Double balance);

    IncomeExpenseResponseDto toIncomeExpenseResponseDto(Double amount);

    GoalStatisticsResponseDto toGoalStatisticsResponseDto(Long completedGoals, Double averageGoalProgress);

    @Mapping(source = "categoryExpenses", target = "categoryExpenses", qualifiedByName = "mapCategoryExpenses")
    ExpenseAnalysisResponseDto toExpenseAnalysisResponseDto(Map<String, Double> categoryExpenses);

    @Named("mapCategoryExpenses")
    static Map<String, Double> mapCategoryExpenses(Map<String, Double> categoryExpenses) {
        return categoryExpenses;
    }
}
