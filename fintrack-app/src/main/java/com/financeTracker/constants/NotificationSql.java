package com.financeTracker.constants;

public interface NotificationSql {
    String INSERT = """
    INSERT INTO fintrack.notification (created_at, user_id, type, read)
    VALUES (?, ?, ?, ?)
    """;

    String FIND_ALL_BY_USER_ID = """
    SELECT * FROM fintrack.notification
    WHERE user_id = ?
    """;

    String FIND_UNREAD_BY_USER_ID = """
    SELECT * FROM fintrack.notification
    WHERE user_id = ? AND read = false
    """;

    String DELETE_BY_USER_ID = """
    DELETE FROM fintrack.notification
    WHERE user_id = ?
    """;

    String READ_ALL_BY_USER_ID = """
            UPDATE fintrack.notification
            SET read = true
            WHERE user_id = ?
            """;
}