package com.financeTracker.controller;

import com.financeTracker.model.entity.User;
import com.financeTracker.service.NotificationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Sends a notification when a goal is achieved.
     *
     * @param user The user whose goal is achieved.
     * @return A boolean indicating whether the notification was successful.
     */
    public boolean notifyGoalAchieved(User user) {
        return notificationService.notifyGoalAchieved(user);
    }

    /**
     * Sends a notification when the budget is exceeded.
     *
     * @param user The user whose budget is exceeded.
     * @return A boolean indicating whether the notification was successful.
     */
    public boolean notifyBudgetExceeded(User user) {
        return notificationService.notifyBudgetExceeded(user);
    }
}
