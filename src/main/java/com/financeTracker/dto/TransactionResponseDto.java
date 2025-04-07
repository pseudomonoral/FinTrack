package com.financeTracker.dto;

import com.financeTracker.model.type.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private double amount;
    private String category;
    private String description;
    private TransactionType type;
    private LocalDateTime createdAt;
}

