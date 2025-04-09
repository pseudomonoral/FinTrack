package com.financeTracker.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetCheckResponseDto {
    private boolean exceeded;
    private double monthlyExpense;
    private double budgetLimit;
}

