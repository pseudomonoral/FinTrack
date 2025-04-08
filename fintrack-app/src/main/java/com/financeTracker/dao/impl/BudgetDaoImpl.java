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
        return jdbcTemplate.query(BudgetSql.FIND_BY_USER_ID, budgetRowMapper, userId)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Budget> findById(Long id) {
        return jdbcTemplate.query(BudgetSql.FIND_BY_ID, budgetRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Budget> findAll() {
        return jdbcTemplate.query(BudgetSql.FIND_ALL, budgetRowMapper);
    }

    @Override
    public Budget save(Budget entity) {
        Long generatedId = jdbcTemplate.queryForObject(
                BudgetSql.INSERT,
                Long.class,
                entity.getUserId(),
                entity.getLimit()
        );
        entity.setId(generatedId);
        return entity;
    }

    @Override
    public void delete(Long budgetId) {
        jdbcTemplate.update(BudgetSql.DELETE, budgetId);
    }

    @Override
    public void updateBudget(Budget entity) {
        jdbcTemplate.update(BudgetSql.UPDATE, entity.getLimit(), entity.getId());
    }
}