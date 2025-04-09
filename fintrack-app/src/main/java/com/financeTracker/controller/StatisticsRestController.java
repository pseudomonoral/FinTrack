package com.financeTracker.controller;

import com.financeTracker.dto.statistic.*;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Tag(name = "Статистика", description = "Получение статистики по балансу, расходам, доходам и целям")
@Validated
public class StatisticsRestController {

    private final StatisticsService statisticsService;

    @Operation(summary = "Текущий баланс пользователя")
    @GetMapping("/current-balance")
    public ResponseEntity<BalanceResponseDto> getCurrentBalance(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user) {
        BalanceResponseDto balance = statisticsService.calculateCurrentBalance(user.getId());
        return ResponseEntity.ok(balance);
    }

    @Operation(summary = "Анализ расходов по категориям")
    @GetMapping("/expense-analysis")
    public ResponseEntity<ExpenseAnalysisResponseDto> getExpenseAnalysis(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user) {
        ExpenseAnalysisResponseDto analysis = statisticsService.analyzeExpensesByCategory(user.getId());
        return ResponseEntity.ok(analysis);
    }

    @Operation(summary = "Анализ выполнения целей пользователя")
    @GetMapping("/goal-statistics")
    public ResponseEntity<GoalStatisticsResponseDto> getGoalStatistics(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user) {
        GoalStatisticsResponseDto statistics = statisticsService.analyzeGoalsByUserId(user.getId());
        return ResponseEntity.ok(statistics);
    }

    @Operation(summary = "Доход за указанный период")
    @PostMapping("/income-for-period")
    public ResponseEntity<IncomeExpenseResponseDto> getIncomeForPeriod(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user,
            @Valid @RequestBody PeriodRequestDto requestDto) {
        IncomeExpenseResponseDto income = statisticsService.calculateIncomeForPeriod(
                user.getId(), requestDto.getStartDate(), requestDto.getEndDate());
        return ResponseEntity.ok(income);
    }

    @Operation(summary = "Расход за указанный период")
    @PostMapping("/expense-for-period")
    public ResponseEntity<IncomeExpenseResponseDto> getExpenseForPeriod(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user,
            @Valid @RequestBody PeriodRequestDto requestDto) {
        IncomeExpenseResponseDto expense = statisticsService.calculateExpenseForPeriod(
                user.getId(), requestDto.getStartDate(), requestDto.getEndDate());
        return ResponseEntity.ok(expense);
    }
}

