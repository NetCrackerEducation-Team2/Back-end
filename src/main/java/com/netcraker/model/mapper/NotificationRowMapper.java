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
                        .creationTime(resultSet.getTimestamp("CREATION_TIME").toLocalDateTime())
                        .isRead(resultSet.getBoolean("OBJECT_READ"))
                        .sendAll(resultSet.getBoolean("SEND_ALL"))
                        .build())
                .notifierId(resultSet.getInt("NOTIFIER_ID"))
                .isRead(resultSet.getBoolean("IS_READ"))
                .build();
    }
}
