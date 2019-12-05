package com.netcraker.model.mapper;

import com.netcraker.model.ChatMessage;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public class ChatMessageRowMapper implements RowMapper<ChatMessage> {
    @Override
    public ChatMessage mapRow(ResultSet resultSet, int i) throws SQLException {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(resultSet.getString("content"));
       // chatMessage.setUser1_id(resultSet.getInt("user_id"));
        return chatMessage;
    }
}
