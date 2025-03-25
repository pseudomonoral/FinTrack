package com.financeTracker.controller;

import com.financeTracker.dto.BudgetCheckResponse;
import com.financeTracker.dto.BudgetCreateRequest;
import com.financeTracker.dto.BudgetResponse;
import com.financeTracker.dto.BudgetUpdateRequest;
import com.financeTracker.mapper.BudgetMapper;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.service.BudgetService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.mapstruct.factory.Mappers;

@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final BudgetMapper budgetMapper = Mappers.getMapper(BudgetMapper.class);

    public BudgetResponse createBudget(Long userId, BudgetCreateRequest request) {
        budgetService.createBudget(userId, request.getLimit());
        return budgetMapper.toResponse(request.getLimit());
    }

    public Optional<BudgetResponse> getBudget(long userId) {
        return budgetService.getBudget(userId).map(budgetMapper::toResponse);
    }

    public BudgetResponse updateBudget(Long userId, BudgetUpdateRequest request) {
        budgetService.updateBudget(userId, request.getNewLimit());
        return budgetMapper.toResponse(request.getNewLimit());
    }

    public void deleteBudget(long userId) {
        budgetService.deleteBudget(userId);
    }

    public BudgetCheckResponse checkBudgetExceeded(long userId, double monthlyExpense) {
        boolean exceeded = budgetService.isBudgetExceeded(userId);
        double limit = budgetService.getBudget(userId)
                .map(Budget::getLimit)
                .orElse(0.0);

        BudgetCheckResponse response = new BudgetCheckResponse();
        response.setExceeded(exceeded);
        response.setMonthlyExpense(monthlyExpense);
        response.setBudgetLimit(limit);

        return response;
    }
}


