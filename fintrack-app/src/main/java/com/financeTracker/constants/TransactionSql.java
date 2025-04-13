package com.financeTracker.constants;

public interface TransactionSql {
    String FIND_BY_ID = """
            SELECT * FROM transactions WHERE id = ?
            """;

    String FIND_ALL_BY_USER_ID = """
            SELECT * FROM transactions WHERE user_id = ?
            """;

    String FIND_ALL = """
            SELECT * FROM transactions
            """;

    String INSERT = """
            INSERT INTO transactions
            (user_id, amount, category, type, description, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, now(), now()) RETURNING id
            """;

    String UPDATE = """
            UPDATE transactions
            SET amount = ?, category = ?, type = ?, description = ?, updated_at = now()
            WHERE id = ?
            """;

    String DELETE = """
            DELETE FROM transactions WHERE id = ?
            """;

    String FIND_ALL_BY_USER_ID_AND_DATE = """
            SELECT *
            FROM transactions
            WHERE user_id = ?
              AND created_at >= ?
            """;

    String FIND_ALL_BY_USER_ID_AND_CATEGORY = """
            SELECT *
            FROM transactions
            WHERE user_id = ?
              AND LOWER(category) = LOWER(?)
            """;

    String FIND_ALL_BY_USER_ID_AND_TYPE = """
            SELECT *
            FROM transactions
            WHERE user_id = ?
              AND type = ?
            """;

    String CALCULATE_TOTAL_FOR_PERIOD = """
            SELECT COALESCE(SUM(amount), 0)
            FROM transactions
            WHERE user_id = ?
              AND type = ?
              AND created_at BETWEEN ? AND ?
            """;
}
