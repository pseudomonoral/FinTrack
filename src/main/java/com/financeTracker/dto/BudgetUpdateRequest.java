package com.financeTracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BudgetUpdateRequest {

    @NotNull(message = "Лимит бюджета не может быть null")
    @Min(value = 0, message = "Лимит бюджета должен быть положительным числом")
    private Double newLimit;
}

