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
        return null;
//        Chat chat = new Chat();
//        chat.setContent(resultSet.getString("content"));
//       // chatMessage.setUser1_id(resultSet.getInt("user_id"));
//        return chat;
    }
}
