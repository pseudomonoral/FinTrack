package com.financeTracker.dto;

import lombok.Data;

@Data
public class BudgetCheckResponse {
    private boolean exceeded;
    private double monthlyExpense;
    private double budgetLimit;
}

