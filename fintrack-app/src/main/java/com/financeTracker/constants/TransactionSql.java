package com.financeTracker.constants;

public interface TransactionSql {
    String FIND_TRANSACTION_BY_ID_SQL = """
            SELECT * FROM transactions WHERE id = ?
            """;

    String FIND_ALL_TRANSACTIONS_BY_USER_ID_SQL = """
            SELECT * FROM transactions WHERE user_id = ?
            """;

    String FIND_ALL_TRANSACTIONS = """
            SELECT * FROM transactions
            """;

    String INSERT_TRANSACTIONS = """
            INSERT INTO transactions
            (user_id, amount, category, type, description, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, now(), now()) RETURNING id
            """;

    String UPDATE_TRANSACTIONS = """
            UPDATE transactions
            SET amount = ?, category = ?, type = ?, description = ?, updated_at = now()
            WHERE id = ?
            """;

    String DELETE = """
            DELETE FROM transactions WHERE id = ?
            """;

    String FIND_ALL_TRANSACTIONS_BY_USER_ID_AND_DATE_SQL = """
            SELECT *
            FROM transactions
            WHERE user_id = ?
              AND created_at >= ?
            """;

    String FIND_ALL_TRANSACTIONS_BY_USER_ID_AND_CATEGORY_SQL = """
            SELECT *
            FROM transactions
            WHERE user_id = ?
              AND LOWER(category) = LOWER(?)
            """;

    String FIND_ALL_TRANSACTIONS_BY_USER_ID_AND_TYPE_SQL = """
            SELECT *
            FROM transactions
            WHERE user_id = ?
              AND type = ?
            """;

    String CALCULATE_TOTAL_FOR_PERIOD_SQL = """
            SELECT COALESCE(SUM(amount), 0)
            FROM transactions
            WHERE user_id = ?
              AND type = ?
              AND created_at BETWEEN ? AND ?
            """;
}
