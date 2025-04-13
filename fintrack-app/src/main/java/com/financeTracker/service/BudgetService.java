package com.financeTracker.service;

import com.financeTracker.dto.budget.BudgetCheckResponseDto;
import com.financeTracker.dto.budget.BudgetRequestDto;
import com.financeTracker.exceptions.entity.EntityNotFoundException;

import java.util.Optional;

/**
 * Service interface for managing user budgets.
 */
public interface BudgetService {

    /**
     * Creates a budget for the specified user.
     *
     * @param userId    the ID of the user for whom the budget is being created.
     * @param budgetRequestDto the DTO containing budget details such as the spending limit.
     */
    void createBudget(Long userId, BudgetRequestDto budgetRequestDto);

    /**
     * Retrieves the budget for the specified user.
     *
     * @param userId the ID of the user whose budget is to be retrieved.
     * @return an {@link Optional} containing the {@link BudgetRequestDto} if the budget exists, or empty if not.
     */
    Optional<BudgetRequestDto> getBudget(long userId);

    /**
     * Updates the existing budget for the specified user.
     *
     * @param userId    the ID of the user whose budget is to be updated.
     * @param budgetRequestDto the DTO containing the updated budget information.
     * @throws EntityNotFoundException if no budget exists for the given user.
     */
    void updateBudget(Long userId, BudgetRequestDto budgetRequestDto);

    /**
     * Deletes the budget for the specified user.
     *
     * @param userId the ID of the user whose budget is to be deleted.
     * @throws EntityNotFoundException if no budget exists for the given user.
     */
    void deleteBudget(long userId);

    /**
     * Checks if the user has exceeded their budget in the past month.
     *
     * @param userId the ID of the user.
     * @return BudgetCheckResponseDto.
     * @throws EntityNotFoundException if no budget exists for the given user.
     */
    BudgetCheckResponseDto isBudgetExceeded(long userId);
}

