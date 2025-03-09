package com.financeTracker.dao;

import com.financeTracker.model.entity.Goal;

/**
 *  Общее:
 *  - Поиск целей по id пользователя
 *  - Установка цели (накопления)
 */

public interface GoalDao extends MainDao<Long, Goal> {

    Goal updateGoal(Goal goal);

}
