package com.netcraker.model.mapper;

import com.netcraker.model.Announcement;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AnnouncementRowMapper implements RowMapper<Announcement> {
    @Override
    public Announcement mapRow(ResultSet resultSet, int i) throws SQLException {
        Announcement announcement = Announcement.builder()
                .announcementId(resultSet.getInt("ANNOUNCEMENT_ID"))
                .title(resultSet.getString("TITLE"))
                .description(resultSet.getString("DESCRIPTION"))
                .userId(resultSet.getInt("USER_ID"))
                .published(resultSet.getBoolean("PUBLISHED"))
                .bookId(resultSet.getInt("BOOK_ID"))
                .creationTime(resultSet.getTimestamp("CREATION_TIME").toLocalDateTime())
                .build();
        return  announcement;
    }
}
