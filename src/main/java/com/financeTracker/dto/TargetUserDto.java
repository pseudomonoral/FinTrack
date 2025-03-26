package com.financeTracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TargetUserDto {
    @NotBlank
    Long userId;
}
