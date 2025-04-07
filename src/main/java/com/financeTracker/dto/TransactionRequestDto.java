package com.financeTracker.dto;

import com.financeTracker.model.type.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {

    @Min(value = 0, message = "Сумма должна быть неотрицательной")
    private double amount;

    @NotBlank(message = "Категория не должна быть пустой")
    private String category;

    private String description;

    @NotNull(message = "Тип транзакции обязателен")
    private TransactionType type;
}

