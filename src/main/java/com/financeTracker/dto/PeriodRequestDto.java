package com.financeTracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodRequestDto {
    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
