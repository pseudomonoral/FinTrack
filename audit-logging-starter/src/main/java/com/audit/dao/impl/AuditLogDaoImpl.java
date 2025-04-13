package com.audit.dao.impl;

import com.audit.dao.AuditLogDao;
import com.audit.model.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.audit.constants.AuditSql.*;

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
        jdbcTemplate.update(INSERT_AUDIT_SQL,
                entry.userLogin(),
                entry.eventType(),
                entry.ipAddress(),
                entry.createdAt()
        );
    }

    @Override
    public List<AuditLog> findByLogin(String login) {
        return jdbcTemplate.query(FIND_BY_LOGIN_AUDIT_SQL, this::mapRow, login);
    }

    @Override
    public List<AuditLog> findByIp(String ip) {
        return jdbcTemplate.query(FIND_BY_IP_AUDIT_SQL, this::mapRow, ip);
    }

    @Transactional
    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_AUDIT_SQL);
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


