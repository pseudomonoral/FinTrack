package com.financeTracker.service;

import com.financeTracker.model.entity.User;

/**
 * The NotificationService interface provides methods for notifying users about
 * important events such as achieving a goal or exceeding their budget.
 */
public interface NotificationService {

    /**
     * Sends a notification to the user when they have achieved a goal.
     *
     * @param user The user who will receive the notification.
     * @return boolean True if the notification was successfully sent, false otherwise.
     */
    boolean notifyGoalAchieved(User user);

    /**
     * Sends a notification to the user when their budget has been exceeded.
     *
     * @param user The user who will receive the notification.
     * @return boolean True if the notification was successfully sent, false otherwise.
     */
    boolean notifyBudgetExceeded(User user);
}

