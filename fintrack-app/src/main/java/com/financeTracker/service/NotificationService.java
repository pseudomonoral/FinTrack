package com.financeTracker.service;

import com.financeTracker.dto.notification.NotificationResponseDto;
import com.financeTracker.dto.user.TargetUserDto;
import com.financeTracker.model.type.NotificationType;

import java.util.List;

/**
 * The NotificationService interface defines operations for managing user notifications.
 * It allows creating, retrieving, and deleting notifications for a specific user.
 */
public interface NotificationService {

    /**
     * Creates a new notification of the given type for the specified user.
     *
     * @param notificationType the type of notification to create.
     * @param targetUserDto    the DTO representing the user for whom the notification is created.
     */
    void createNotification(NotificationType notificationType, TargetUserDto targetUserDto);

    /**
     * Retrieves all notifications for the specified user.
     *
     * @param targetUserDto the DTO representing the user whose notifications are to be fetched.
     * @return a list of all {@link NotificationResponseDto} for the user.
     */
    List<NotificationResponseDto> getAllByUserId(TargetUserDto targetUserDto);

    /**
     * Retrieves only unread notifications for the specified user.
     *
     * @param targetUserDto the DTO representing the user whose unread notifications are to be fetched.
     * @return a list of unread {@link NotificationResponseDto} for the user.
     */
    List<NotificationResponseDto> getUnreadByUserId(TargetUserDto targetUserDto);

    /**
     * Deletes all notifications for the specified user.
     *
     * @param targetUserDto the DTO representing the user whose notifications should be deleted.
     */
    void deleteAllByUserId(TargetUserDto targetUserDto);
}
