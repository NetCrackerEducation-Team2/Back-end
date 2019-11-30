package com.netcraker.model.mapper;

import com.netcraker.model.NotificationMessage;
import com.netcraker.model.NotificationObject;
import com.netcraker.model.NotificationType;
import com.netcraker.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NotificationObjectRowMapper implements RowMapper<NotificationObject> {
    @Override
    public NotificationObject mapRow(ResultSet resultSet, int i) throws SQLException {
        return NotificationObject.builder()
                .notificationObjectId(resultSet.getInt("NOTIFICATION_OBJECT_ID"))
                .entityId(resultSet.getInt("ENTITY_ID"))
                .isRead(resultSet.getBoolean("IS_READ"))
                .sendAll(resultSet.getBoolean("SEND_ALL"))
                .creationTime(resultSet.getTimestamp("CREATION_TIME").toLocalDateTime())
                .notificationType(NotificationType.builder()
                        .notificationTypeId(resultSet.getInt("NOTIFICATION_TYPE_ID"))
                        .notificationTypeName(resultSet.getString("NOTIFICATION_TYPE_NAME"))
                        .build())
                .notificationMessage(NotificationMessage.builder()
                        .notificationMessageId(resultSet.getInt("NOTIFICATION_MESSAGE_ID"))
                        .notificationMessageText(resultSet.getString("NOTIFICATION_MESSAGE_TEXT"))
                        .build())
                .user(User.builder()
                        .userId(resultSet.getInt("USER_ID"))
                        .fullName(resultSet.getString("FULL_NAME"))
                        .build())
                .build();
    }
}
