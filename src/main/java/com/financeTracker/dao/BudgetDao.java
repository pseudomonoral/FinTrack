package com.financeTracker.dao;

import com.financeTracker.model.entity.Budget;

import java.util.Optional;

/**
 * Общее:
 * - Установка месячного бюджета.
 * - Поиск бюджета по id пользователя
 */

public interface BudgetDao extends MainDao<Long, Budget>{

    Budget updateBudget(Budget budget);

    Optional<Budget> findByUserId(Long userId);
}
