package com.financeTracker.constants;

public interface TransactionSql {
    String FIND_BY_ID = "SELECT * FROM fintrack.transactions WHERE id = ?";
    String FIND_ALL_BY_USER_ID = "SELECT * FROM fintrack.transactions WHERE user_id = ?";
    String FIND_ALL = "SELECT * FROM fintrack.transactions";

    String INSERT = """
            INSERT INTO fintrack.transactions
            (user_id, amount, category, type, description, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, now(), now()) RETURNING id
            """;

    String UPDATE = """
            UPDATE fintrack.transactions
            SET amount = ?, category = ?, type = ?, description = ?, updated_at = now()
            WHERE id = ?
            """;

    String DELETE = "DELETE FROM fintrack.transactions WHERE id = ?";

    String FIND_ALL_BY_USER_ID_AND_DATE = """
        SELECT *
        FROM fintrack.transactions
        WHERE user_id = ?
          AND created_at >= ?
        """;

    String FIND_ALL_BY_USER_ID_AND_CATEGORY = """
        SELECT *
        FROM fintrack.transactions
        WHERE user_id = ?
          AND LOWER(category) = LOWER(?)
        """;

    String FIND_ALL_BY_USER_ID_AND_TYPE = """
        SELECT *
        FROM fintrack.transactions
        WHERE user_id = ?
          AND type = ?
        """;

    String CALCULATE_TOTAL_FOR_PERIOD = """
        SELECT COALESCE(SUM(amount), 0)
        FROM fintrack.transactions
        WHERE user_id = ?
          AND type = ?
          AND created_at BETWEEN ? AND ?
        """;
}

