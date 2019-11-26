package com.netcraker.repositories.impl;

import com.netcraker.model.Notification;
import com.netcraker.model.mapper.NotificationRowMapper;
import com.netcraker.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
@RequiredArgsConstructor
public class NotificationRepositoryImp implements NotificationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NotificationRowMapper notificationRowMapper;

    @Value("${notifications.getById}")
    private String sqlGetById;

    @Value("${notifications.count}")
    private String sqlGetCount;

    @Override
    public Optional<List<Notification>> getUserNotifications(int user_id, int limit, int offset) {
        List<Notification> notifications =  jdbcTemplate.query(sqlGetById, notificationRowMapper, new Object[] {user_id, limit, offset});
        return notifications.isEmpty() ? Optional.empty() : Optional.of(notifications);
    }

    @Override
    public Optional<Notification> getById(int user_id) {
        List<Notification> notifications =  jdbcTemplate.query(sqlGetById, notificationRowMapper, user_id);
        return notifications.isEmpty() ? Optional.empty() : Optional.of(notifications.get(0));
    }

    @Override
    public Optional<Notification> insert(Notification entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Notification> update(Notification entity) {
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public int getCount(int user_id) {
        return jdbcTemplate.queryForObject(sqlGetCount, new Object[] { user_id }, int.class);
    }
}
