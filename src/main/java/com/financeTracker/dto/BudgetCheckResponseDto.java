package com.financeTracker.dto;

import lombok.Data;

@Data
public class BudgetCheckResponseDto {
    private boolean exceeded;
    private double monthlyExpense;
    private double budgetLimit;
}

