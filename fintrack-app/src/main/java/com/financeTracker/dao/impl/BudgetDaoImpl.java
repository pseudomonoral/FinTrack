package com.financeTracker.dao.impl;


import com.financeTracker.constants.BudgetSql;
import com.financeTracker.dao.BudgetDao;
import com.financeTracker.model.entity.Budget;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.financeTracker.constants.BudgetSql.*;

@Repository
@RequiredArgsConstructor
public class BudgetDaoImpl implements BudgetDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Budget> findByUserId(Long userId) {
        return jdbcTemplate.query(FIND_BUDGET_BY_USER_ID_SQL, budgetRowMapper, userId)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Budget> findById(Long id) {
        return jdbcTemplate.query(FIND_BUDGET_BY_ID_SQL, budgetRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Budget> findAll() {
        return jdbcTemplate.query(FIND_ALL_BUDGETS_SQL, budgetRowMapper);
    }

    @Override
    public Budget save(Budget entity) {
        Long generatedId = jdbcTemplate.queryForObject(
                INSERT_BUDGET_SQL,
                Long.class,
                entity.getUserId(),
                entity.getLimit()
        );
        entity.setId(generatedId);
        return entity;
    }

    @Override
    public void delete(Long budgetId) {
        jdbcTemplate.update(DELETE_BUDGET_SQL, budgetId);
    }

    @Override
    public void updateBudget(Budget entity) {
        jdbcTemplate.update(UPDATE_BUDGET_SQL, entity.getLimit(), entity.getId());
    }

    private final RowMapper<Budget> budgetRowMapper = (rs, rowNum) -> Budget.builder()
            .id(rs.getLong("id"))
            .userId(rs.getLong("user_id"))
            .limit(rs.getBigDecimal("limit_budget").doubleValue())
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .build();
}