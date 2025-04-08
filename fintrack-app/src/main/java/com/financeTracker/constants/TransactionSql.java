package com.financeTracker.constants;

public interface TransactionSql {
    String FIND_BY_USER_ID_LIMIT_1 = "SELECT * FROM fintrack.transactions WHERE user_id = ? LIMIT 1";
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
}

