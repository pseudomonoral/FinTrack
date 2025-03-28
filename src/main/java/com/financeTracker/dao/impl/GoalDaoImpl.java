package com.financeTracker.dao.impl;

import com.financeTracker.dao.GoalDao;
import com.financeTracker.model.entity.Goal;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;


@RequiredArgsConstructor
public class GoalDaoImpl implements GoalDao {
    private final DataSource dataSource;


    @Override
    public List<Goal> findAllByUserId(Long userId) {
        String sql = "SELECT * FROM fintrack.goals WHERE user_id = ?";
        List<Goal> goals = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                goals.add(mapToGoalFromDatabase(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении целей пользователя", e);
        }
        return goals;
    }

    @Override
    public Optional<Goal> findGoalByNameAndId(String goalName, Long userId) {
        String sql = "SELECT * FROM fintrack.goals WHERE name = ? AND user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, goalName);
            stmt.setLong(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToGoalFromDatabase(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске цели", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Goal> findById(Long id) {
        String sql = "SELECT * FROM fintrack.goals WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToGoalFromDatabase(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске цели по ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Goal> findAll() {
        String sql = "SELECT * FROM fintrack.goals";
        List<Goal> goals = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                goals.add(mapToGoalFromDatabase(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех целей", e);
        }
        return goals;
    }

    @Override
    public Goal save(Goal entity) {
        String sql = "INSERT INTO fintrack.goals " +
                "(user_id, name, target_amount, current_amount, goal_completed, goal_actual, " +
                "created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, now(), now()) RETURNING id";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, entity.getUserId());
                stmt.setString(2, entity.getName());
                stmt.setBigDecimal(3, BigDecimal.valueOf(entity.getTargetAmount()));
                stmt.setBigDecimal(4, BigDecimal.valueOf(entity.getCurrentAmount()));
                stmt.setBoolean(5, entity.isGoalCompleted());
                stmt.setBoolean(6, entity.isGoalActual());
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Ошибка при сохранении цели", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка работы с базой данных", e);
        }

        return entity;
    }

    @Override
    public void delete(Long goalId) {
        String sql = "DELETE FROM fintrack.goals WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, goalId);
                stmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Ошибка при удалении цели", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка работы с базой данных", e);
        }
    }

    @Override
    public void updateGoal(Goal entity) {
        String sql = "UPDATE fintrack.goals " +
                "SET name = ?, target_amount = ?, current_amount = ?, goal_completed = ?, " +
                "goal_actual = ?, updated_at = now() " +
                "WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, entity.getName());
                stmt.setBigDecimal(2, BigDecimal.valueOf(entity.getTargetAmount()));
                stmt.setBigDecimal(3, BigDecimal.valueOf(entity.getCurrentAmount()));
                stmt.setBoolean(4, entity.isGoalCompleted());
                stmt.setBoolean(5, entity.isGoalActual());
                stmt.setLong(6, entity.getId());

                stmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Ошибка при обновлении цели", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка работы с базой данных", e);
        }
    }

    private Goal mapToGoalFromDatabase(ResultSet rs) throws SQLException {
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


