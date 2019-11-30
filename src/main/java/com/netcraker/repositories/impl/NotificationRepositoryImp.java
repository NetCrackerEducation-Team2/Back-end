package com.netcraker.repositories.impl;

import com.netcraker.model.Notification;
import com.netcraker.model.mapper.NotificationRowMapper;
import com.netcraker.repositories.GenericRepository;
import com.netcraker.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
@RequiredArgsConstructor
public class NotificationRepositoryImp implements NotificationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NotificationRowMapper notificationRowMapper;
    private static final Logger logger = LoggerFactory.getLogger(NotificationRepositoryImp.class);
    private final GenericRepository<Notification, NotificationRowMapper> genericRepository;

    @Value("${notifications.getByNotifierId}")
    private String sqlGetByNotifierId;
    @Value("${notifications.getById}")
    private String sqlGetById;
    @Value("${notifications.count}")
    private String sqlGetCount;
    @Value("${notifications.insert}")
    private String sqlInsert;
    @Value("${notifications.update}")
    private String sqlUpdate;
    @Value("${notifications.delete}")
    private String sqlDelete;

    @Override
    public Optional<List<Notification>> getUserNotifications(int user_id, int limit, int offset) {
        logger.info("Trying to get user`s notification with user_id = " + user_id);
        List<Notification> notifications =  jdbcTemplate.query(sqlGetByNotifierId, notificationRowMapper, new Object[] {user_id, limit, offset});
        return notifications.isEmpty() ? Optional.empty() : Optional.of(notifications);
    }

    @Override
    public Optional<Notification> getById(int id) {
        return genericRepository.getById(notificationRowMapper, sqlGetById, id);
    }

    @Override
    public Optional<Notification> insert(Notification entity) {
        logger.info("trying to add notification to db: " + entity);

        //Object[] params = {entity.getNotificationObject().getNotificationObjectId(), entity.getNotifierId()};
        //jdbcTemplate.update(sqlInsert, params);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, entity.getNotificationObject().getNotificationObjectId());
            ps.setInt(2, entity.getNotifierId());
            return ps;
        }, keyHolder);

        return getById((Integer) keyHolder.getKeys().get("notification_id"));
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
