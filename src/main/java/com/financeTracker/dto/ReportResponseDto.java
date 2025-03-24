package com.financeTracker.dto;

import lombok.Data;

@Data
public class ReportResponseDto {
    private double currentBalance;
    private String budgetState;
    private String goalState;
    private double monthlyExpenses;
    private double monthlyIncome;
    private double totalExpenses;
    private double totalIncome;
    private Long completedGoals;
    private Double averageGoalProgress;
}
