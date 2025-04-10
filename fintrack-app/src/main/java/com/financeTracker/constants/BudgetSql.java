package com.financeTracker.constants;

public interface BudgetSql {
    String FIND_BY_USER_ID = """
            SELECT * FROM fintrack.budgets WHERE user_id = ?
            """;

    String FIND_BY_ID = """
            SELECT * FROM fintrack.budgets WHERE id = ?
            """;

    String FIND_ALL = """
            SELECT * FROM fintrack.budgets
            """;

    String INSERT = """
            INSERT INTO fintrack.budgets
            (user_id, `limit`, created_at, updated_at)
            VALUES (?, ?, now(), now()) RETURNING id
            """;

    String UPDATE = """
            UPDATE fintrack.budgets
            SET `limit` = ?, updated_at = now() WHERE id = ?
            """;

    String DELETE = """
            DELETE FROM fintrack.budgets WHERE id = ?
            """;
}