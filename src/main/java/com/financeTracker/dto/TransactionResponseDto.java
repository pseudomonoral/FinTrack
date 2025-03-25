package com.financeTracker.dto;

import com.financeTracker.model.type.TransactionType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDto {
    private Long id;
    private double amount;
    private String category;
    private String description;
    private TransactionType type;
    private LocalDateTime createdAt;
}

