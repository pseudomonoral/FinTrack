package com.financeTracker.dao;

import com.financeTracker.model.entity.Notification;

import java.util.List;

/**
 * The NotificationDao interface defines low-level database operations
 * for working with {@link Notification} entities.
 * <p>
 * It provides methods to save, retrieve, and delete notifications by user ID.
 */
public interface NotificationDao {

    /**
     * Persists the given {@link Notification} in the database.
     *
     * @param notification the notification entity to be saved.
     */
    void save(Notification notification);

    /**
     * Retrieves all notifications associated with the specified user.
     *
     * @param userId the ID of the user whose notifications are to be retrieved.
     * @return a list of all {@link Notification} entities for the user.
     */
    List<Notification> findAllByUserId(Long userId);

    /**
     * Retrieves only unread notifications for the specified user.
     *
     * @param userId the ID of the user whose unread notifications are to be retrieved.
     * @return a list of unread {@link Notification} entities for the user.
     */
    List<Notification> findUnreadByUserId(Long userId);

    /**
     * Deletes all notifications associated with the specified user.
     *
     * @param userId the ID of the user whose notifications should be deleted.
     */
    void deleteAllByUserId(Long userId);

    /**
     * Marks all notifications as read for the specified user.
     *
     * @param userId the ID of the user whose notifications should be marked as read.
     */
    void readAllByUserId(Long userId);
}

