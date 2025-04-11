package com.financeTracker.dao.impl;

import com.financeTracker.constants.NotificationSql;
import com.financeTracker.dao.NotificationDao;
import com.financeTracker.model.entity.Notification;
import com.financeTracker.model.type.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationDaoImpl implements NotificationDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Notification notification) {
        jdbcTemplate.update(NotificationSql.INSERT,
                notification.getCreatedAt(),
                notification.getUserId(),
                notification.getType().name(),
                notification.isRead());
    }

    @Override
    public List<Notification> findAllByUserId(Long userId) {
        return jdbcTemplate.query(NotificationSql.FIND_ALL_BY_USER_ID,
                this::mapToNotificationFromDatabase, userId);
    }

    @Override
    public List<Notification> findUnreadByUserId(Long userId) {
        return jdbcTemplate.query(NotificationSql.FIND_UNREAD_BY_USER_ID,
                this::mapToNotificationFromDatabase, userId);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        jdbcTemplate.update(NotificationSql.DELETE_BY_USER_ID, userId);
    }

    @Override
    public void readAllByUserId(Long userId) {
        jdbcTemplate.update(NotificationSql.READ_ALL_BY_USER_ID, userId);
    }

    private Notification mapToNotificationFromDatabase(ResultSet rs, int rowNum) throws SQLException {
        return Notification.builder()
                .id(rs.getLong("id"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .userId(rs.getLong("user_id"))
                .type(NotificationType.valueOf(rs.getString("type")))
                .read(rs.getBoolean("read"))
                .build();
    }
}

