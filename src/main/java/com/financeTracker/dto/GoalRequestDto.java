package com.financeTracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GoalRequestDto {
    @NotBlank(message = "Название цели не должно быть пустым")
    private String name;

    @Min(value = 1, message = "Целевая сумма должна быть больше 0")
    private double targetAmount;

    @Min(value = 0, message = "Текущая сумма должна быть больше 0")
    private double currentAmount;

    @NotNull(message = "Актуальность цели должна быть указана")
    private Boolean goalActive;
}