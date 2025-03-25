package com.financeTracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminActionDto {
    @NotNull
    private Long userId;
}

