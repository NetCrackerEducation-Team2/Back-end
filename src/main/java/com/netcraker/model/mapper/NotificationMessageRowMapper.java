package com.netcraker.model.mapper;

import com.netcraker.model.NotificationMessage;
import lombok.Builder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
public class NotificationMessageRowMapper implements RowMapper<NotificationMessage> {
    @Override
    public NotificationMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
        return NotificationMessage.builder()
                .notificationMessageId(rs.getInt("notification_message_id"))
                .notificationMessageText(rs.getString("notification_message_text"))
                .build();
    }
}
