package com.financeTracker.dao.impl;

import com.financeTracker.dao.UserDao;
import com.financeTracker.model.entity.User;
import com.financeTracker.model.type.BudgetStatus;
import com.financeTracker.model.type.GoalStatus;
import com.financeTracker.model.type.UserRole;
import com.financeTracker.model.type.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = "SELECT * FROM fintrack.users WHERE login = ?";
        return jdbcTemplate.query(sql, this::mapToUser, login).stream().findFirst();
    }

    @Override
    public void update(User user) {
        String sql = """
                UPDATE fintrack.users 
                SET login = ?, password = ?, name = ?, user_role = ?, 
                    user_status = ?, goal_status = ?, budget_status = ?, updated_at = now() 
                WHERE id = ?
                """;
        jdbcTemplate.update(sql,
                user.getLogin(),
                user.getPassword(),
                user.getName(),
                user.getUserRole().name(),
                user.getUserStatus().name(),
                user.getGoalStatus().name(),
                user.getBudgetStatus().name(),
                user.getId());
    }

    @Override
    public void delete(Long userId) {
        String sql = "DELETE FROM fintrack.users WHERE id = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM fintrack.users WHERE id = ?";
        return jdbcTemplate.query(sql, this::mapToUser, id).stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM fintrack.users";
        return jdbcTemplate.query(sql, this::mapToUser);
    }

    @Override
    public User save(User entity) {
        String sql = """
                INSERT INTO fintrack.users 
                (login, password, name, user_role, user_status, goal_status, budget_status, created_at, updated_at) 
                VALUES (?, ?, ?, ?, ?, ?, ?, now(), now()) RETURNING id
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, entity.getLogin());
            stmt.setString(2, entity.getPassword());
            stmt.setString(3, entity.getName());
            stmt.setString(4, entity.getUserRole().name());
            stmt.setString(5, entity.getUserStatus().name());
            stmt.setString(6, entity.getGoalStatus().name());
            stmt.setString(7, entity.getBudgetStatus().name());
            return stmt;
        }, keyHolder);

        entity.setId(keyHolder.getKey().longValue());
        return entity;
    }

    private User mapToUser(ResultSet rs, int rowNum) throws SQLException {
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
