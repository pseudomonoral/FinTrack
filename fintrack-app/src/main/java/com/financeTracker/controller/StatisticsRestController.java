package com.financeTracker.controller;

import com.financeTracker.dto.statistic.*;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.service.StatisticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsRestController {

    private final StatisticsService statisticsService;

    @GetMapping("/current-balance")
    public ResponseEntity<BalanceResponseDto> getCurrentBalance(@SessionAttribute("user") UserDto user) {
        BalanceResponseDto balance = statisticsService.calculateCurrentBalance(user.getId());
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/expense-analysis")
    public ResponseEntity<ExpenseAnalysisResponseDto> getExpenseAnalysis(@SessionAttribute("user") UserDto user) {
        ExpenseAnalysisResponseDto analysis = statisticsService.analyzeExpensesByCategory(user.getId());
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/goal-statistics")
    public ResponseEntity<GoalStatisticsResponseDto> getGoalStatistics(@SessionAttribute("user") UserDto user) {
        GoalStatisticsResponseDto statistics = statisticsService.analyzeGoalsByUserId(user.getId());
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/income-for-period")
    public ResponseEntity<IncomeExpenseResponseDto> getIncomeForPeriod(
            @SessionAttribute("user") UserDto user,
            @RequestBody @Valid PeriodRequestDto requestDto) {
        IncomeExpenseResponseDto income = statisticsService.calculateIncomeForPeriod(
                user.getId(), requestDto.getStartDate(), requestDto.getEndDate());
        return ResponseEntity.ok(income);
    }

    @PostMapping("/expense-for-period")
    public ResponseEntity<IncomeExpenseResponseDto> getExpenseForPeriod(
            @SessionAttribute("user") UserDto user,
            @RequestBody @Valid PeriodRequestDto requestDto) {
        IncomeExpenseResponseDto expense = statisticsService.calculateExpenseForPeriod(
                user.getId(), requestDto.getStartDate(), requestDto.getEndDate());
        return ResponseEntity.ok(expense);
    }
}
