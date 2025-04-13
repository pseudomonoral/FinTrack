package com.financeTracker.constants;

public interface BudgetSql {
    String FIND_BY_USER_ID = """
            SELECT * FROM budgets WHERE user_id = ?
            """;

    String FIND_BY_ID = """
            SELECT * FROM budgets WHERE id = ?
            """;

    String FIND_ALL = """
            SELECT * FROM budgets
            """;

    String INSERT = """
            INSERT INTO budgets
            (user_id, limit_budget, created_at, updated_at)
            VALUES (?, ?, now(), now()) RETURNING id
            """;

    String UPDATE = """
            UPDATE budgets
            SET limit_budget = ?, updated_at = now() WHERE id = ?
            """;

    String DELETE = """
            DELETE FROM fintrack.budgets WHERE id = ?
            """;
}