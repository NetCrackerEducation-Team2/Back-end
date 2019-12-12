package com.netcraker.model.mapper;

import com.netcraker.model.Chat;
import com.netcraker.model.Message;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public class MessageRowMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet resultSet, int i) throws SQLException {
        Message message = new Message();
        message.setContent(resultSet.getString("content"));
        message.setFromUser(resultSet.getInt("user_id"));
        message.setChatId(resultSet.getInt("chat_id"));
        return message;
    }
}
