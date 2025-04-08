package com.financeTracker.dao.impl;

import com.financeTracker.constants.UserSql;
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
import java.util.Objects;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findByLogin(String login) {
        return jdbcTemplate.query(UserSql.FIND_BY_LOGIN, this::mapToUser, login).stream().findFirst();
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(UserSql.UPDATE,
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
        jdbcTemplate.update(UserSql.DELETE, userId);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jdbcTemplate.query(UserSql.FIND_BY_ID, this::mapToUser, id).stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(UserSql.FIND_ALL, this::mapToUser);
    }

    @Override
    public User save(User entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(UserSql.INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, entity.getLogin());
            stmt.setString(2, entity.getPassword());
            stmt.setString(3, entity.getName());
            stmt.setString(4, entity.getUserRole().name());
            stmt.setString(5, entity.getUserStatus().name());
            stmt.setString(6, entity.getGoalStatus().name());
            stmt.setString(7, entity.getBudgetStatus().name());
            return stmt;
        }, keyHolder);

        entity.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
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