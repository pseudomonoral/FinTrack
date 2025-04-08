package com.financeTracker.dao.impl;


import com.financeTracker.constants.TransactionSql;
import com.financeTracker.dao.TransactionDao;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransactionDaoImpl implements TransactionDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Transaction> findByUserId(Long userId) {
        return jdbcTemplate.query(TransactionSql.FIND_BY_USER_ID_LIMIT_1, this::mapToTransaction, userId)
                .stream().findFirst();
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        jdbcTemplate.update(TransactionSql.UPDATE,
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getType().name(),
                transaction.getDescription(),
                transaction.getId());
    }

    @Override
    public void delete(Long transactionId) {
        jdbcTemplate.update(TransactionSql.DELETE, transactionId);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return jdbcTemplate.query(TransactionSql.FIND_BY_ID, this::mapToTransaction, id)
                .stream().findFirst();
    }

    @Override
    public List<Transaction> findAllByUserId(Long id) {
        return jdbcTemplate.query(TransactionSql.FIND_ALL_BY_USER_ID, this::mapToTransaction, id);
    }

    @Override
    public List<Transaction> findAll() {
        return jdbcTemplate.query(TransactionSql.FIND_ALL, this::mapToTransaction);
    }

    @Override
    public Transaction save(Transaction entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(TransactionSql.INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, entity.getUserId());
            stmt.setBigDecimal(2, BigDecimal.valueOf(entity.getAmount()));
            stmt.setString(3, entity.getCategory());
            stmt.setString(4, entity.getType().name());
            stmt.setString(5, entity.getDescription());
            return stmt;
        }, keyHolder);

        entity.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return entity;
    }

    private Transaction mapToTransaction(ResultSet rs, int rowNum) throws SQLException {
        return Transaction.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .amount(rs.getBigDecimal("amount").doubleValue())
                .category(rs.getString("category"))
                .type(TransactionType.valueOf(rs.getString("type")))
                .description(rs.getString("description"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }
}