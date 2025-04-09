package com.financeTracker.constants;

public interface NotificationSql {
    String INSERT = "INSERT INTO notification (created_at, user_id, type, read) VALUES (?, ?, ?, ?)";
    String FIND_UNREAD_BY_USER_ID = "SELECT id, created_at, user_id, type, read FROM notification WHERE user_id = ? AND read = false ORDER BY created_at DESC";
    String FIND_ALL_BY_USER_ID = "SELECT id, created_at, user_id, type, read FROM notification WHERE user_id = ? ORDER BY created_at DESC";
    String DELETE_BY_USER_ID = "DELETE FROM notification WHERE user_id = ?";
}