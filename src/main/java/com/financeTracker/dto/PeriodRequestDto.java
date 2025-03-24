package com.financeTracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PeriodRequestDto {
    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
