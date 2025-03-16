package com.financeTracker.dao.impl;

import com.financeTracker.dao.BudgetDao;
import com.financeTracker.model.entity.Budget;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@RequiredArgsConstructor
public class BudgetDaoImpl implements BudgetDao {
    private final DataSource dataSource;


    @Override
    public Optional<Budget> findByUserId(Long userId) {
        String sql = "SELECT * FROM fintrack.budgets WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToBudget(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске бюджета по user_id", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Budget> findById(Long id) {
        String sql = "SELECT * FROM fintrack.budgets WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapToBudget(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске бюджета по ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Budget> findAll() {
        String sql = "SELECT * FROM fintrack.budgets";
        List<Budget> budgets = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                budgets.add(mapToBudget(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех бюджетов", e);
        }
        return budgets;
    }

    @Override
    public Budget save(Budget entity) {
        String sql = "INSERT INTO fintrack.budgets (id, user_id, limit, created_at, updated_at) " +
                "VALUES (nextval('fintrack.budget_seq'), ?, ?, now(), now()) RETURNING id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, entity.getUserId());
            stmt.setBigDecimal(2, BigDecimal.valueOf(entity.getLimit()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении бюджета", e);
        }
        return entity;
    }

    @Override
    public void delete(Budget entity) {
        String sql = "DELETE FROM fintrack.budgets WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении бюджета", e);
        }
    }

    @Override
    public Budget updateBudget(Budget entity) {
        String sql = "UPDATE fintrack.budgets SET limit = ?, updated_at = now() WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, BigDecimal.valueOf(entity.getLimit()));
            stmt.setLong(2, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении бюджета", e);
        }
        return entity;
    }

    private Budget mapToBudget(ResultSet rs) throws SQLException {
        return Budget.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .limit(rs.getBigDecimal("limit").doubleValue())
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }
}


