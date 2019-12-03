package com.netcraker.repositories.impl;

import com.netcraker.model.NotificationObject;
import com.netcraker.model.mapper.NotificationObjectRowMapper;
import com.netcraker.repositories.NotificationObjectRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
public class NotificationObjectRepositoryImp extends GenericRepositoryImp<NotificationObject> implements NotificationObjectRepository {
    private static final Logger logger = LoggerFactory.getLogger(NotificationRepositoryImp.class);
    private final NotificationObjectRowMapper notificationObjectRowMapper;
    @Value("${notification_objects.getById}")
    private String sqlGetById;
    @Value("${notification_objects.insert}")
    private String sqlInsert;
    @Value("${notification_objects.update}")
    private String sqlUpdate;
    @Value("${notification_objects.delete}")
    private String sqlDelete;


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
    protected PreparedStatement setInsertQueryParams(NotificationObject entity, PreparedStatement ps) {
        setParams(entity, ps, 1);
        return ps;
    }

    @Override
    protected String getSqlUpdateQuery() {
        return sqlUpdate;
    }

    @Override
    protected PreparedStatement setUpdateQueryParams(NotificationObject entity, PreparedStatement ps) {
        try {
            ps.setInt(setParams(entity, ps, 1), entity.getId());
            return ps;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getIdColumnName() {
        return "notification_object_id";
    }

    @Override
    protected String getSqlDeleteQuery() {
        return sqlDelete;
    }

    private int setParams(NotificationObject entity, PreparedStatement ps, int firstParamIndex) {
        try {
            int curParamIndex = firstParamIndex;
            ps.setInt(curParamIndex++, entity.getNotificationType().getNotificationTypeId());
            ps.setInt(curParamIndex++, entity.getEntityId());
            ps.setInt(curParamIndex++, entity.getUser().getUserId());
            ps.setInt(curParamIndex++, entity.getNotificationMessage().getNotificationMessageId());
            ps.setTimestamp(curParamIndex++, new Timestamp(entity.getCreationTime().atZone(ZoneId.systemDefault()).toEpochSecond()));
            ps.setBoolean(curParamIndex++, entity.isSendAll());
            ps.setBoolean(curParamIndex++, entity.isRead());
            return curParamIndex;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
