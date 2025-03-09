package com.financeTracker.service;

import com.financeTracker.model.entity.User;

public interface NotificationService {

    boolean notifyGoalAchieved(User user);

    boolean notifyBudgetExceeded(User user);
}
