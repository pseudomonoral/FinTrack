package com.financeTracker.dao.impl;

import com.financeTracker.dao.GoalDao;
import com.financeTracker.model.entity.Goal;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;


@Repository
@RequiredArgsConstructor
public class GoalDaoImpl implements GoalDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Goal> findAllByUserId(Long userId) {
        String sql = "SELECT * FROM fintrack.goals WHERE user_id = ?";
        return jdbcTemplate.query(sql, this::mapToGoalFromDatabase, userId);
    }

    @Override
    public Optional<Goal> findGoalByNameAndId(String goalName, Long userId) {
        String sql = "SELECT * FROM fintrack.goals WHERE name = ? AND user_id = ?";
        return jdbcTemplate.query(sql, this::mapToGoalFromDatabase, goalName, userId)
                .stream().findFirst();
    }

    @Override
    public Optional<Goal> findById(Long id) {
        String sql = "SELECT * FROM fintrack.goals WHERE id = ?";
        return jdbcTemplate.query(sql, this::mapToGoalFromDatabase, id)
                .stream().findFirst();
    }

    @Override
    public List<Goal> findAll() {
        String sql = "SELECT * FROM fintrack.goals";
        return jdbcTemplate.query(sql, this::mapToGoalFromDatabase);
    }

    @Override
    public Goal save(Goal entity) {
        String sql = """
                INSERT INTO fintrack.goals 
                (user_id, name, target_amount, current_amount, goal_completed, goal_actual, created_at, updated_at) 
                VALUES (?, ?, ?, ?, ?, ?, now(), now()) RETURNING id
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, entity.getUserId());
            stmt.setString(2, entity.getName());
            stmt.setBigDecimal(3, BigDecimal.valueOf(entity.getTargetAmount()));
            stmt.setBigDecimal(4, BigDecimal.valueOf(entity.getCurrentAmount()));
            stmt.setBoolean(5, entity.isGoalCompleted());
            stmt.setBoolean(6, entity.isGoalActual());
            return stmt;
        }, keyHolder);

        entity.setId(keyHolder.getKey().longValue());
        return entity;
    }

    @Override
    public void delete(Long goalId) {
        String sql = "DELETE FROM fintrack.goals WHERE id = ?";
        jdbcTemplate.update(sql, goalId);
    }

    @Override
    public void updateGoal(Goal entity) {
        String sql = """
                UPDATE fintrack.goals 
                SET name = ?, target_amount = ?, current_amount = ?, goal_completed = ?, goal_actual = ?, updated_at = now() 
                WHERE id = ?
                """;
        jdbcTemplate.update(sql,
                entity.getName(),
                entity.getTargetAmount(),
                entity.getCurrentAmount(),
                entity.isGoalCompleted(),
                entity.isGoalActual(),
                entity.getId());
    }

    private Goal mapToGoalFromDatabase(ResultSet rs, int rowNum) throws SQLException {
        return Goal.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .name(rs.getString("name"))
                .targetAmount(rs.getBigDecimal("target_amount").doubleValue())
                .currentAmount(rs.getBigDecimal("current_amount").doubleValue())
                .goalCompleted(rs.getBoolean("goal_completed"))
                .goalActual(rs.getBoolean("goal_actual"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }
}



