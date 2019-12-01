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
        logger.info("Trying to get notification object with notification_object_id = " + id);
        //List<NotificationObject> list =  jdbcTemplate.query(sqlGetById, notificationObjectRowMapper, new Object[] {id});
        return genericRepository.getById(NotificationObject.class, notificationObjectRowMapper, id);
    }

    @Override
    public Optional<NotificationObject> insert(NotificationObject entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, entity.getNotificationType().getNotificationTypeId());
            ps.setInt(2, entity.getEntityId());
            ps.setInt(3, entity.getUser().getUserId());
            ps.setInt(4, entity.getNotificationMessage().getNotificationMessageId());
            ps.setBoolean(5, entity.isSendAll());
            return ps;
        }, keyHolder);

        return getById((Integer) keyHolder.getKeys().get("notification_object_id"));
    }

    @Override
    public Optional<NotificationObject> update(NotificationObject entity) {
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
