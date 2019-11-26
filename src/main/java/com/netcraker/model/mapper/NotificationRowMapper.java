package com.netcraker.model.mapper;

import com.netcraker.model.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NotificationRowMapper implements RowMapper<Notification> {
    @Override
    public Notification mapRow(ResultSet resultSet, int i) throws SQLException {
        return Notification.builder()
                .notificationId(resultSet.getInt("NOTIFICATION_ID"))
                .notificationObject(NotificationObject.builder()
                        .notificationObjectId(resultSet.getInt("NOTIFICATION_OBJECT_ID"))
                        .entityId(resultSet.getInt("ENTITY_ID"))
                        .entityType(EntityType.builder()
                                .entityTypeId(resultSet.getInt("ENTITY_TYPE_ID"))
                                .name(resultSet.getString("NAME"))
                                .build())
                        .action(NotificationAction.builder()
                                .notificationActionId(resultSet.getInt("NOTIFICATION_ACTION_ID"))
                                .description(resultSet.getString("DESCRIPTION"))
                                .build())
                        .user(User.builder()
                                .userId(resultSet.getInt("USER_ID"))
                                .fullName(resultSet.getString("FULL_NAME"))
                                .build())
                        .creationTime(resultSet.getTimestamp("CREATION_TIME").toLocalDateTime())
                        .build())
                .notifierId(resultSet.getInt("NOTIFIER_ID"))
                .isRead(resultSet.getBoolean("STATUS"))
                .build();
    }
}
