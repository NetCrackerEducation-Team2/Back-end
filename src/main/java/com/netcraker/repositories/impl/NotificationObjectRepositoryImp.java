package com.netcraker.repositories.impl;

import com.netcraker.model.NotificationObject;
import com.netcraker.model.mapper.NotificationObjectRowMapper;
import com.netcraker.repositories.GenericRepository;
import com.netcraker.repositories.NotificationObjectRepository;
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
public class NotificationObjectRepositoryImp extends GenericRepositoryImp<NotificationObject> implements NotificationObjectRepository {
   // private final JdbcTemplate jdbcTemplate;
    private final NotificationObjectRowMapper notificationObjectRowMapper;
    @Value("${notification_objects.getById}")
    private String sqlGetById;
    @Value("${notification_objects.insert}")
    private String sqlInsert;
    @Value("${notification_objects.update}")
    private String sqlUpdate;
    @Value("${notification_objects.delete}")
    private String sqlDelete;
    @Value("${notification_objects.count}")
    private String sqlGetCount;

    public NotificationObjectRepositoryImp(NotificationObjectRowMapper notificationObjectRowMapper, JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.notificationObjectRowMapper = notificationObjectRowMapper;
    }

    @Override
    protected RowMapper<NotificationObject> getRowMapper() {
        return notificationObjectRowMapper;
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
    protected int setParams(NotificationObject entity, PreparedStatement ps, int firstParamIndex) {
        try {
            int curParamIndex = firstParamIndex;
            ps.setInt(curParamIndex++, entity.getNotificationType().getNotificationTypeId());
            ps.setInt(curParamIndex++, entity.getEntityId());
            ps.setInt(curParamIndex++, entity.getUser().getUserId());
            ps.setInt(curParamIndex++, entity.getNotificationMessage().getNotificationMessageId());
            ps.setBoolean(curParamIndex++, entity.isSendAll());
            ps.setBoolean(curParamIndex++, entity.isRead());
            return curParamIndex;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
