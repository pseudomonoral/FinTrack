package com.financeTracker.service.impl;

import com.financeTracker.model.entity.User;
import com.financeTracker.service.NotificationService;
import com.financeTracker.service.UserService;
import lombok.RequiredArgsConstructor;

import static com.financeTracker.model.type.BudgetStatus.OVER_BUDGET;
import static com.financeTracker.model.type.GoalStatus.*;

@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserService userService;

    @Override
    public boolean notifyGoalAchieved(User user) {
        if (user.getGoalStatus().equals(ACHIEVED)){
            user.setGoalStatus(NOT_ACHIEVED);
            userService.updateUser(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean notifyBudgetExceeded(User user) {
        return user.getBudgetStatus().equals(OVER_BUDGET);
    }
}
