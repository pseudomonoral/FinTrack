package com.financeTracker.dao.impl;

import com.financeTracker.dao.UserDao;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.BudgetStatus;
import com.financeTracker.model.type.GoalStatus;
import com.financeTracker.model.type.UserRole;
import com.financeTracker.model.type.UserStatus;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final DataSource dataSource;

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = "SELECT * FROM fintrack.users WHERE login = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя по логину", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE fintrack.users " +
                "SET login = ?, password = ?, name = ?, user_role = ?, user_status = ?, goal_status = ?, budget_status = ?, updated_at = now() " +
                "WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            user.onUpdate();
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getName());
            stmt.setObject(4, user.getUserRole().name());
            stmt.setObject(5, user.getUserStatus().name());
            stmt.setObject(6, user.getGoalStatus().name());
            stmt.setObject(7, user.getBudgetStatus().name());
            stmt.setLong(8, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении пользователя", e);
        }
    }

    @Override
    public void delete(User user) {
        String sql = "DELETE FROM fintrack.users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM fintrack.users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя по ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM fintrack.users";
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка пользователей", e);
        }
        return users;
    }

    @Override
    public User save(User entity) {
        String sql = "INSERT INTO fintrack.users (login, password, name, user_role, user_status, goal_status, budget_status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, now(), now()) RETURNING id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getLogin());
            stmt.setString(2, entity.getPassword());
            stmt.setString(3, entity.getName());
            stmt.setObject(4, entity.getUserRole().name());
            stmt.setObject(5, entity.getUserStatus().name());
            stmt.setObject(6, entity.getGoalStatus().name());
            stmt.setObject(7, entity.getBudgetStatus().name());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении пользователя", e);
        }
        return entity;
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .login(rs.getString("login"))
                .password(rs.getString("password"))
                .name(rs.getString("name"))
                .userRole(UserRole.valueOf(rs.getString("user_role")))
                .userStatus(UserStatus.valueOf(rs.getString("user_status")))
                .goalStatus(GoalStatus.valueOf(rs.getString("goal_status")))
                .budgetStatus(BudgetStatus.valueOf(rs.getString("budget_status")))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }

}


