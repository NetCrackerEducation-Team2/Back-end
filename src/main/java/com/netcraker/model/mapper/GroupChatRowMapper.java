package com.netcraker.model.mapper;

import com.netcraker.model.Chat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupChatRowMapper  implements RowMapper<Chat> {
    @Override
    public Chat mapRow(ResultSet resultSet, int i) throws SQLException {
        Chat chat = new Chat();
        chat.setChatName(resultSet.getString("name"));
        chat.setChatId(resultSet.getInt("chat_id"));
        chat.setGroupChatId(resultSet.getInt("group_chat_id"));
        return chat;
    }
}
