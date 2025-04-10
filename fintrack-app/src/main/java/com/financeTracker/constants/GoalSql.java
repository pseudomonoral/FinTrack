package com.financeTracker.constants;

public interface GoalSql {
    String FIND_ALL_BY_USER_ID = """
            SELECT * FROM fintrack.goals WHERE user_id = ?
            """;

    String FIND_BY_NAME_AND_USER_ID = """
            SELECT * FROM fintrack.goals WHERE name = ? AND user_id = ?
            """;

    String FIND_BY_ID = """
            SELECT * FROM fintrack.goals WHERE id = ?
            """;

    String FIND_ALL = """
            SELECT * FROM fintrack.goals
            """;

    String INSERT = """
            INSERT INTO fintrack.goals
            (user_id, name, target_amount, current_amount, goal_completed, goal_actual, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, now(), now()) RETURNING id
            """;

    String DELETE = """
            DELETE FROM fintrack.goals WHERE id = ?
            """;

    String UPDATE = """
            UPDATE fintrack.goals
            SET name = ?, target_amount = ?, current_amount = ?, goal_completed = ?, goal_actual = ?, updated_at = now()
            WHERE id = ?
            """;
}
