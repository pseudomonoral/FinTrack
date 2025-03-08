package com.financeTracker.dao.impl;

import com.financeTracker.dao.BudgetDao;
import com.financeTracker.model.entity.Budget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BudgetDaoImpl implements BudgetDao {

    private final Map<Long, Budget> budgets = new HashMap<>();

    private Long id = 1L;

    public BudgetDaoImpl() {
    }

    @Override
    public Optional<Budget> findById(Long id) {
        return Optional.ofNullable(budgets.get(id));
    }

    @Override
    public List<Budget> findAll() {
        return List.copyOf(budgets.values());
    }

    @Override
    public Budget save(Budget entity) {
        entity.setId(id++);
        budgets.put(entity.getId(), entity);
        return budgets.get(entity.getId());
    }

    @Override
    public void delete(Budget entity) {
        if (budgets.containsValue(entity)) {
            budgets.remove(entity.getId());
        } else {
            throw new IllegalArgumentException("budget: \""
                    + entity.toString() + "\" doesn't exist");
        }
    }
}
