package com.financeTracker.mapper;

import com.financeTracker.model.entity.Budget;

public class BudgetOldMapper {

    public Budget mapToBudget(Long userId, double limit) {
        return Budget.builder()
                .userId(userId)
                .limit(limit)
                .build();
    }
}
