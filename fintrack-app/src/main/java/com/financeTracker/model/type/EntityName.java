package com.financeTracker.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityName {
    BUDGET("Budget"),
    GOAL("Goal"),
    TRANSACTION("Transaction"),
    USER("User");

    private final String value;
}

