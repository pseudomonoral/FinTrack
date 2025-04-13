package com.financeTracker.constants;

public interface NotificationSql {
    String INSERT_NOTIFICATION_SQL = """
    INSERT INTO notification (created_at, user_id, type, read)
    VALUES (?, ?, ?, ?)
    """;

    String FIND_ALL_NOTIFICATIONS_BY_USER_ID_SQL = """
    SELECT * FROM notification
    WHERE user_id = ?
    """;

    String FIND_UNREAD_NOTIFICATIONS_BY_USER_ID_SQL = """
    SELECT * FROM notification
    WHERE user_id = ? AND read = false
    """;

    String DELETE_NOTIFICATIONS_BY_USER_ID_SQL = """
    DELETE FROM notification
    WHERE user_id = ?
    """;

    String READ_ALL_NOTIFICATIONS_BY_USER_ID_SQL = """
            UPDATE notification
            SET read = true
            WHERE user_id = ?
            """;
}