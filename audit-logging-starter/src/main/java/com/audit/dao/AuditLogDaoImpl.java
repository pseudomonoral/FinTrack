package com.audit.dao;

import com.audit.model.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditLogDaoImpl implements AuditLogDao {

    private final JdbcTemplate jdbcTemplate;

    public AuditLogDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    @Override
    public void save(AuditLog entry) {
        jdbcTemplate.update("""
                INSERT INTO fintrack.audit_log (user_login, event_type, ip_address, created_at)
                VALUES (?, ?, ?, ?)
                """,
                entry.userLogin(),
                entry.eventType(),
                entry.ipAddress(),
                entry.createdAt()
        );
    }

    @Override
    public List<AuditLog> findByLogin(String login) {
        return jdbcTemplate.query("""
                SELECT user_login, event_type, ip_address, created_at
                FROM fintrack.audit_log
                WHERE user_login = ?
                ORDER BY created_at DESC
                """, this::mapRow, login);
    }

    @Override
    public List<AuditLog> findByIp(String ip) {
        return jdbcTemplate.query("""
                SELECT user_login, event_type, ip_address, created_at
                FROM fintrack.audit_log
                WHERE ip_address = ?
                ORDER BY created_at DESC
                """, this::mapRow, ip);
    }

    @Transactional
    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM fintrack.audit_log");
    }

    private AuditLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AuditLog(
                rs.getString("user_login"),
                rs.getString("event_type"),
                rs.getString("ip_address"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}


