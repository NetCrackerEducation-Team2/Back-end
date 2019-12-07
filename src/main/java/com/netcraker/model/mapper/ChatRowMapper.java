package com.netcraker.model.mapper;

import com.netcraker.model.Chat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatRowMapper implements RowMapper<Chat> {

    @Override
    public Chat mapRow(ResultSet resultSet, int i) throws SQLException {
        Chat chat = new Chat();
        chat.setFriendId(resultSet.getInt("user1_id"));
        chat.setUserCurrentId(resultSet.getInt("user2_id"));
        return chat;
    }
}
