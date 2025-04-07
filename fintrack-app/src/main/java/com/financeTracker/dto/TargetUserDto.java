package com.financeTracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetUserDto {
    @NotNull(message = "User ID must not be null")
    @Positive(message = "User ID must be positive")
    private Long userId;
}
