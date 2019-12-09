package com.netcraker.repositories.impl;

import com.netcraker.model.NotificationMessage;
import com.netcraker.model.mapper.NotificationMessageRowMapper;
import com.netcraker.repositories.NotificationMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class NotificationMessageRepositoryImpl extends GenericRepositoryImp<NotificationMessage> implements NotificationMessageRepository {
    private final NotificationMessageRowMapper rowMapper = NotificationMessageRowMapper.builder().build();
    @Value("${notificationMessage.getById}")
    private String sqlById;
    @Value("${notificationMessage.insert}")
    private String sqlInsert;
    public NotificationMessageRepositoryImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<NotificationMessage> getRowMapper() {
        return rowMapper;
    }

    @Override
    protected String getSqlGetByIdQuery() {
        return sqlById;
    }

    @Override
    protected String getSqlInsertQuery() {
        return sqlInsert;
    }

    @Override
    protected String getSqlUpdateQuery() {
        // TODO implement this if needed
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getSqlDeleteQuery() {
        // TODO implement this if needed
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getSqlCountQuery() {
        // TODO implement this if needed
        throw new UnsupportedOperationException();
    }

    @Override
    protected int setParams(NotificationMessage entity, PreparedStatement ps, int firstParamIndex) {
        try {
            int curParamIndex = firstParamIndex;
            ps.setString(curParamIndex++, entity.getNotificationMessageText());
            return curParamIndex;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
