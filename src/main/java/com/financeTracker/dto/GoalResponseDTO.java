package com.financeTracker.dto;

import lombok.Data;

@Data
public class GoalResponseDTO {
    private Long id;
    private String name;
    private double targetAmount;
    private double currentAmount;
    private boolean goalActual;
    private double progress;
}

