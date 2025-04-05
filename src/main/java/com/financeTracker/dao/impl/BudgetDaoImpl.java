package com.financeTracker.dao.impl;


import com.financeTracker.dao.BudgetDao;
import com.financeTracker.model.entity.Budget;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BudgetDaoImpl implements BudgetDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Budget> budgetRowMapper = (rs, rowNum) -> Budget.builder()
            .id(rs.getLong("id"))
            .userId(rs.getLong("user_id"))
            .limit(rs.getBigDecimal("limit").doubleValue())
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .build();

    @Override
    public Optional<Budget> findByUserId(Long userId) {
        String sql = "SELECT * FROM fintrack.budgets WHERE user_id = ?";
        return jdbcTemplate.query(sql, budgetRowMapper, userId).stream().findFirst();
    }

    @Override
    public Optional<Budget> findById(Long id) {
        String sql = "SELECT * FROM fintrack.budgets WHERE id = ?";
        return jdbcTemplate.query(sql, budgetRowMapper, id).stream().findFirst();
    }

    @Override
    public List<Budget> findAll() {
        String sql = "SELECT * FROM fintrack.budgets";
        return jdbcTemplate.query(sql, budgetRowMapper);
    }

    @Override
    public Budget save(Budget entity) {
        String sql = "INSERT INTO fintrack.budgets (user_id, \"limit\", created_at, updated_at) " +
                "VALUES (?, ?, now(), now()) RETURNING id";

        Long generatedId = jdbcTemplate.queryForObject(sql, Long.class, entity.getUserId(), entity.getLimit());
        entity.setId(generatedId);
        return entity;
    }

    @Override
    public void delete(Long budgetId) {
        String sql = "DELETE FROM fintrack.budgets WHERE id = ?";
        jdbcTemplate.update(sql, budgetId);
    }

    @Override
    public void updateBudget(Budget entity) {
        String sql = "UPDATE fintrack.budgets SET \"limit\" = ?, updated_at = now() WHERE id = ?";
        jdbcTemplate.update(sql, entity.getLimit(), entity.getId());
    }
}
