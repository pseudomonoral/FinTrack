package com.financeTracker.dto.budget;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDto {
    @NotNull(message = "Лимит бюджета не может быть null")
    @Min(value = 0, message = "Лимит бюджета должен быть положительным числом")
    private Double limit;
}