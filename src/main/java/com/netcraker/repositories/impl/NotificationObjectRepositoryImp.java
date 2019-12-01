package com.netcraker.repositories.impl;

import com.netcraker.model.NotificationObject;
import com.netcraker.model.mapper.NotificationObjectRowMapper;
import com.netcraker.repositories.GenericRepository;
import com.netcraker.repositories.NotificationObjectRepository;
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
public class NotificationObjectRepositoryImp implements NotificationObjectRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NotificationObjectRowMapper notificationObjectRowMapper;
    private static final Logger logger = LoggerFactory.getLogger(NotificationRepositoryImp.class);
    private final GenericRepository<NotificationObject, NotificationObjectRowMapper> genericRepository;
    @Value("${notification_objects.getById}")
    private String sqlGetById;
    @Value("${notification_objects.insert}")
    private String sqlInsert;
    @Value("${notification_objects.update}")
    private String sqlUpdate;
    @Value("${notification_objects.delete}")
    private String sqlDelete;

    @Override
    public Optional<NotificationObject> getById(int id) {
        return genericRepository.getById(NotificationObject.class, notificationObjectRowMapper, id);
    }

    @Override
    public Optional<NotificationObject> insert(NotificationObject entity) {
        Object[] params = {entity.getNotificationType().getNotificationTypeId(),
                entity.getEntityId(), entity.getUser().getUserId(), entity.getNotificationMessage().getNotificationMessageId(),
                entity.isSendAll()};
        return genericRepository.insert(entity, notificationObjectRowMapper, params);
    }

    @Override
    public Optional<NotificationObject> update(NotificationObject entity) {
        Object[] params = {entity.getNotificationType().getNotificationTypeId(),
                entity.getEntityId(), entity.getUser().getUserId(), entity.getNotificationMessage().getNotificationMessageId(),
                entity.isSendAll(), entity.isRead(), entity.getNotificationObjectId()};
        return genericRepository.update(entity, notificationObjectRowMapper, params, entity.getNotificationObjectId());
    }

    @Override
    public boolean delete(int id) {
        return genericRepository.delete(NotificationObject.class, id);
    }
}
