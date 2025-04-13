package com.financeTracker.constants;

public interface UserSql {
    String FIND_USER_BY_LOGIN_SQL = """
            SELECT * FROM users WHERE login = ?
            """;

    String FIND_USER_BY_ID_SQL = """
            SELECT * FROM users WHERE id = ?
            """;

    String FIND_ALL_USERS_SQL = """
            SELECT * FROM users
            """;

    String DELETE_USER_SQL = """
            DELETE FROM users WHERE id = ?
            """;

    String INSERT_USER_SQL = """
            INSERT INTO users
            (login, password, name, user_role, user_status, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, now(), now()) RETURNING id
            """;

    String UPDATE_USER_SQL = """
            UPDATE users
            SET login = ?, password = ?, name = ?, user_role = ?,
                user_status = ?, updated_at = now()
            WHERE id = ?
            """;
}
