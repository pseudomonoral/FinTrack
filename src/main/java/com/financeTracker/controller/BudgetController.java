package com.financeTracker.controller;

import com.financeTracker.dto.BudgetCheckResponseDto;
import com.financeTracker.dto.BudgetRequestDto;
import com.financeTracker.dto.BudgetResponseDto;
import com.financeTracker.mapper.DtoBudgetMapper;
import com.financeTracker.model.entity.Budget;
import com.financeTracker.service.BudgetService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.mapstruct.factory.Mappers;

@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final DtoBudgetMapper budgetMapper = Mappers.getMapper(DtoBudgetMapper.class);

    public BudgetResponseDto createBudget(Long userId, BudgetRequestDto request) {
        budgetService.createBudget(userId, request.getLimit());
        return budgetMapper.toResponse(request.getLimit());
    }

    public Optional<BudgetResponseDto> getBudget(long userId) {
        return budgetService.getBudget(userId).map(budgetMapper::toResponse);
    }

    public BudgetResponseDto updateBudget(Long userId, BudgetRequestDto request) {
        budgetService.updateBudget(userId, request.getLimit());
        return budgetMapper.toResponse(request.getLimit());
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


