package com.netcraker.model.mapper;

import com.netcraker.model.Chat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupChatReferenceRowMapper implements RowMapper<Chat> {
    @Override
    public Chat mapRow(ResultSet resultSet, int i) throws SQLException {
        Chat chat = new Chat();
        chat.setGroupChatUsersId(resultSet.getInt("group_chat_users_id"));
        chat.setGroupChatId(resultSet.getInt("group_chat_id"));
        chat.setFriendId(resultSet.getInt("user_id"));
        return chat;
    }
}
