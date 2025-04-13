package com.financeTracker.constants;

public interface GoalSql {
    String FIND_ALL_GOALS_BY_USER_ID_SQL = """
            SELECT * FROM goals WHERE user_id = ?
            """;

    String FIND_GOALS_BY_NAME_AND_USER_ID_SQL = """
            SELECT * FROM goals WHERE name = ? AND user_id = ?
            """;

    String FIND_GOAL_BY_ID_SQL = """
            SELECT * FROM goals WHERE id = ?
            """;

    String FIND_ALL_GOALS_SQL = """
            SELECT * FROM goals
            """;

    String INSERT_GOAL_SQL = """
            INSERT INTO goals
            (user_id, name, target_amount, current_amount, goal_completed, goal_actual, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, now(), now()) RETURNING id
            """;

    String DELETE_GOAL_SQL = """
            DELETE FROM goals WHERE id = ?
            """;

    String UPDATE_GOAL_SQL = """
            UPDATE goals
            SET name = ?, target_amount = ?, current_amount = ?, goal_completed = ?, goal_actual = ?, updated_at = now()
            WHERE id = ?
            """;
}
