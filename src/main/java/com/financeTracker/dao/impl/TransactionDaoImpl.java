package com.financeTracker.dao.impl;

import com.financeTracker.dao.TransactionDao;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.util.DatabaseManager;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class TransactionDaoImpl implements TransactionDao {
    private final DataSource dataSource;

    public TransactionDaoImpl() {
        this.dataSource = DatabaseManager.getDataSource();
    }

    @Override
    public Optional<Transaction> findByUserId(Long userId) {
        String sql = "SELECT * FROM fintrack.transactions WHERE user_id = ? LIMIT 1";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске транзакции по userId", e);
        }
        return Optional.empty();
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        String sql = "UPDATE fintrack.transactions " +
                "SET amount = ?, category = ?, type = ?, description = ?, updated_at = now() " +
                "WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, BigDecimal.valueOf(transaction.getAmount()));
            stmt.setString(2, transaction.getCategory());
            stmt.setString(3, transaction.getType().name());
            stmt.setString(4, transaction.getDescription()); // Добавлено поле description
            stmt.setLong(5, transaction.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении транзакции", e);
        }
        return transaction;
    }

    @Override
    public void delete(Transaction transaction) {
        String sql = "DELETE FROM fintrack.transactions WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, transaction.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении транзакции", e);
        }
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        String sql = "SELECT * FROM fintrack.transactions WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске транзакции по ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT * FROM fintrack.transactions";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                transactions.add(mapToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка транзакций", e);
        }
        return transactions;
    }

    @Override
    public Transaction save(Transaction entity) {
        String sql = "INSERT INTO fintrack.transactions (id, user_id, amount, category, type, description, created_at, updated_at) " +
                "VALUES (nextval('fintrack.transaction_seq'), ?, ?, ?, ?, ?, now(), now()) RETURNING id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getUserId());
            stmt.setBigDecimal(2, BigDecimal.valueOf(entity.getAmount()));
            stmt.setString(3, entity.getCategory());
            stmt.setString(4, entity.getType().name());
            stmt.setString(5, entity.getDescription());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении транзакции", e);
        }
        return entity;
    }

    private Transaction mapToTransaction(ResultSet rs) throws SQLException {
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


