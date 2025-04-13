package com.financeTracker.constants;

public interface BudgetSql {
    String FIND_BUDGET_BY_USER_ID_SQL = """
            SELECT * FROM budgets WHERE user_id = ?
            """;

    String FIND_BUDGET_BY_ID_SQL = """
            SELECT * FROM budgets WHERE id = ?
            """;

    String FIND_ALL_BUDGETS_SQL = """
            SELECT * FROM budgets
            """;

    String INSERT_BUDGET_SQL = """
            INSERT INTO budgets
            (user_id, limit_budget, created_at, updated_at)
            VALUES (?, ?, now(), now()) RETURNING id
            """;

    String UPDATE_BUDGET_SQL = """
            UPDATE budgets
            SET limit_budget = ?, updated_at = now() WHERE id = ?
            """;

    String DELETE_BUDGET_SQL = """
            DELETE FROM fintrack.budgets WHERE id = ?
            """;
}