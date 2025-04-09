package com.financeTracker.dao;

import com.financeTracker.model.entity.Notification;

import java.util.List;

public interface NotificationDao {
    void save(Notification notification);

    List<Notification> findAllByUserId(Long userId);

    List<Notification> findUnreadByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
