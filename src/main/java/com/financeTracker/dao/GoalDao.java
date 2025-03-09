package com.financeTracker.dao;

import com.financeTracker.model.entity.Goal;

import java.util.List;
import java.util.Optional;

/**
 *  Общее:
 *  - Поиск целей по id пользователя
 *  - Установка цели (накопления)
 */

public interface GoalDao extends MainDao<Long, Goal> {

    Goal updateGoal(Goal goal);

    List<Goal> findAllByUserId(Long userId);

    Optional<Goal> findGoalByNameAndId(String goalName, Long userId);
}
