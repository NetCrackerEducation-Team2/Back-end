package com.netcraker.repositories.impl;

import com.netcraker.model.Notification;
import com.netcraker.model.mapper.NotificationRowMapper;
import com.netcraker.repositories.GenericRepository;
import com.netcraker.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
//@RequiredArgsConstructor
@Slf4j
public class NotificationRepositoryImp extends GenericRepositoryImp<Notification> implements NotificationRepository {
    //private final JdbcTemplate jdbcTemplate;
    private final NotificationRowMapper notificationRowMapper;

    @Value("${notifications.getById}")
    private String sqlGetById;
    @Value("${notifications.insert}")
    private String sqlInsert;
    @Value("${notifications.update}")
    private String sqlUpdate;
    @Value("${notifications.getByNotifierId}")
    private String sqlGetByNotifierId;
    @Value("${notifications.getUserNotificationsCount}")
    private String sqlGetUserNotificationsCount;
    @Value("${notifications.delete}")
    private String sqlDelete;
    @Value("${notifications.count}")
    private String sqlGetCount;


    public NotificationRepositoryImp(NotificationRowMapper notificationObjectRowMapper, JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.notificationRowMapper = notificationObjectRowMapper;
    }
    @Override
    public Optional<List<Notification>> getUserNotifications(int user_id, int limit, int offset) {
        log.info("Trying to get user`s notification with user_id = " + user_id);
        List<Notification> notifications =  jdbcTemplate.query(sqlGetByNotifierId, notificationRowMapper, new Object[] {user_id, limit, offset});
        return notifications.isEmpty() ? Optional.empty() : Optional.of(notifications);
    }

    @Override
    protected RowMapper<Notification> getRowMapper() {
        return notificationRowMapper;
    }


    @Override
    protected String getSqlGetByIdQuery() {
        return sqlGetById;
    }

    @Override
    protected String getSqlInsertQuery() {
        return sqlInsert;
    }

    @Override
    protected String getSqlUpdateQuery() {
        return sqlUpdate;
    }

    @Override
    protected String getSqlDeleteQuery() {
        return sqlDelete;
    }

    @Override
    protected String getSqlCountQuery() {
        return sqlGetCount;
    }


    @Override
    public int getUserNotificationsCount(int user_id) {
        return jdbcTemplate.queryForObject(sqlGetUserNotificationsCount, new Object[] { user_id }, int.class);
    }

    @Override
    protected int setParams(Notification entity, PreparedStatement ps, int firstParamIndex) {
        try {
            int curParamIndex = firstParamIndex;
            ps.setInt(curParamIndex++, entity.getNotificationObject().getNotificationObjectId());
            ps.setInt(curParamIndex++, entity.getNotifierId());
            return curParamIndex;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
