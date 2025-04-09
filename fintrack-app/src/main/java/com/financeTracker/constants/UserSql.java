package com.financeTracker.constants;

public interface UserSql {
    String FIND_BY_LOGIN = "SELECT * FROM fintrack.users WHERE login = ?";
    String FIND_BY_ID = "SELECT * FROM fintrack.users WHERE id = ?";
    String FIND_ALL = "SELECT * FROM fintrack.users";
    String DELETE = "DELETE FROM fintrack.users WHERE id = ?";

    String INSERT = """
            INSERT INTO fintrack.users
            (login, password, name, user_role, user_status, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, now(), now()) RETURNING id
            """;

    String UPDATE = """
            UPDATE fintrack.users
            SET login = ?, password = ?, name = ?, user_role = ?,
                user_status = ?, updated_at = now()
            WHERE id = ?
            """;
}

