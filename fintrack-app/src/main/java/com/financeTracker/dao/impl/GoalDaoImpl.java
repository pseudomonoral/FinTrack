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

import static com.financeTracker.constants.GoalSql.*;


@Repository
@RequiredArgsConstructor
public class GoalDaoImpl implements GoalDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Goal> findAllByUserId(Long userId) {
        return jdbcTemplate.query(FIND_ALL_GOALS_BY_USER_ID_SQL, this::mapToGoalFromDatabase, userId);
    }

    @Override
    public Optional<Goal> findGoalByNameAndId(String goalName, Long userId) {
        return jdbcTemplate.query(FIND_GOALS_BY_NAME_AND_USER_ID_SQL, this::mapToGoalFromDatabase, goalName, userId)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Goal> findById(Long id) {
        return jdbcTemplate.query(FIND_GOAL_BY_ID_SQL, this::mapToGoalFromDatabase, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Goal> findAll() {
        return jdbcTemplate.query(FIND_ALL_GOALS_SQL, this::mapToGoalFromDatabase);
    }

    @Override
    public Goal save(Goal entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(INSERT_GOAL_SQL, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, entity.getUserId());
            stmt.setString(2, entity.getName());
            stmt.setBigDecimal(3, BigDecimal.valueOf(entity.getTargetAmount()));
            stmt.setBigDecimal(4, BigDecimal.valueOf(entity.getCurrentAmount()));
            stmt.setBoolean(5, entity.isGoalCompleted());
            stmt.setBoolean(6, entity.isGoalActual());
            return stmt;
        }, keyHolder);

        entity.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return entity;
    }

    @Override
    public void delete(Long goalId) {
        jdbcTemplate.update(DELETE_GOAL_SQL, goalId);
    }

    @Override
    public void updateGoal(Goal entity) {
        jdbcTemplate.update(UPDATE_GOAL_SQL,
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