package com.netcraker.model.mapper;

import com.netcraker.model.Announcement;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnnouncementRowMapper implements RowMapper<Announcement> {
    @Override
    public Announcement mapRow(ResultSet resultSet, int i) throws SQLException {
        Announcement announcement = Announcement.builder()
                .announcement_id(resultSet.getInt("ANNOUNCEMENT_ID"))
                .title(resultSet.getString("TITLE"))
                .description(resultSet.getString("DESCRIPTION"))
                .user_id(resultSet.getInt("USER_ID"))
                .published(resultSet.getBoolean("PUBLISHED"))
                .book_id(resultSet.getInt("BOOK_ID"))
                .build();
        return  announcement;
    }
}
