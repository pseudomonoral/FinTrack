package com.financeTracker.dao.impl;

import com.financeTracker.dao.GoalDao;
import com.financeTracker.model.entity.Goal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GoalDaoImpl implements GoalDao {

    private final Map<Long, Goal> goals = new HashMap<>();

    private Long id = 1L;

    public GoalDaoImpl() {
    }


    @Override
    public Optional<Goal> findById(Long id) {
        return Optional.ofNullable(goals.get(id));
    }

    @Override
    public List<Goal> findAll() {
        return List.copyOf(goals.values());
    }

    @Override
    public Goal save(Goal entity) {
        entity.setId(id++);
        goals.put(entity.getId(), entity);
        return goals.get(entity.getId());
    }

    @Override
    public void delete(Goal entity) {
        if (goals.containsValue(entity)) {
            goals.remove(entity.getId());
        } else {
            throw new IllegalArgumentException("Goal: \""
                    + entity.toString() + "\" doesn't exist");
        }
    }

    @Override
    public Goal updateGoal(Goal entity) {
        if (goals.containsValue(entity)) {
            return goals.put(entity.getId(), entity);
        } else {
            throw new IllegalArgumentException("Goal: \""
                    + entity.toString() + "\" doesn't exist");
        }
    }
}
