package com.financeTracker.dao;

import com.financeTracker.dao.impl.NotificationDaoImpl;
import com.financeTracker.model.entity.Notification;
import com.financeTracker.model.type.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.*;

class NotificationDaoTest extends AbstractDaoTest {

    private NotificationDaoImpl notificationDao;
    private Long userId;

    @BeforeEach
    void setUpDao() {
        notificationDao = new NotificationDaoImpl(jdbcTemplate);
        userId = super.insertTestUserWithRandomId();
    }

    @Test
    void save_shouldInsertNotificationWithDefaults() {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(NotificationType.GOAL_ACHIEVED)
                .build();
        notification.onCreate();

        notificationDao.save(notification);

        List<Notification> notifications = notificationDao.findAllByUserId(userId);
        assertEquals(1, notifications.size());
        Notification saved = notifications.get(0);

        assertEquals(NotificationType.GOAL_ACHIEVED, saved.getType());
        assertFalse(saved.isRead());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void findUnreadByUserId_shouldReturnOnlyUnread() {
        Notification read = Notification.builder()
                .userId(userId)
                .type(NotificationType.BUDGET_EXCEEDED)
                .read(true)
                .build();
        read.onCreate();

        Notification unread = Notification.builder()
                .userId(userId)
                .type(NotificationType.GOAL_ACHIEVED)
                .build();
        unread.onCreate();

        notificationDao.save(read);
        notificationDao.save(unread);

        List<Notification> unreadList = notificationDao.findUnreadByUserId(userId);
        assertEquals(1, unreadList.size());
        assertEquals(NotificationType.GOAL_ACHIEVED, unreadList.get(0).getType());
    }

    @Test
    void deleteAllByUserId_shouldRemoveNotifications() {
        Notification n1 = Notification.builder()
                .userId(userId)
                .type(NotificationType.GOAL_ACHIEVED)
                .build();
        n1.onCreate();

        Notification n2 = Notification.builder()
                .userId(userId)
                .type(NotificationType.BUDGET_EXCEEDED)
                .build();
        n2.onCreate();

        notificationDao.save(n1);
        notificationDao.save(n2);

        notificationDao.deleteAllByUserId(userId);

        List<Notification> remaining = notificationDao.findAllByUserId(userId);
        assertTrue(remaining.isEmpty());
    }
}