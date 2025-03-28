package com.financeTracker.controller;

import com.financeTracker.dto.BudgetCheckResponseDto;
import com.financeTracker.dto.BudgetDto;
import com.financeTracker.mapper.BudgetMapper;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.service.BudgetService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final BudgetMapper budgetMapper = BudgetMapper.INSTANCE;

    public void createBudget(Long userId, BudgetDto request) {
        budgetService.createBudget(userId, request.getLimit());
    }

    public Optional<BudgetDto> getBudget(long userId) {
        return budgetService.getBudget(userId).map(budgetMapper::toBudgetDto);
    }

    public void updateBudget(Long userId, BudgetDto request) {
        budgetService.updateBudget(userId, request.getLimit());
    }

    public void deleteBudget(long userId) {
        budgetService.deleteBudget(userId);
    }

    public BudgetCheckResponseDto checkBudgetExceeded(long userId, double monthlyExpense) {
        boolean exceeded = budgetService.isBudgetExceeded(userId);
        double limit = budgetService.getBudget(userId)
                .map(Budget::getLimit)
                .orElse(0.0);

        BudgetCheckResponseDto response = new BudgetCheckResponseDto();
        response.setExceeded(exceeded);
        response.setMonthlyExpense(monthlyExpense);
        response.setBudgetLimit(limit);

        return response;
    }
}


