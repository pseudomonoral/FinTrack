package com.financeTracker.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ExpenseAnalysisResponseDto {
    private Map<String, Double> categoryExpenses;
}
