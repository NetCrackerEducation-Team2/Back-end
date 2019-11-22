package com.netcraker.model.mapper;

import com.netcraker.model.BookOverview;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class BookOverviewRowMapper implements RowMapper<BookOverview> {

    @Override
    public BookOverview mapRow(ResultSet resultSet, int i) throws SQLException {
        return BookOverview.builder()
                .bookOverviewId(resultSet.getInt("book_overview_id"))
                .description(resultSet.getString("description"))
                .userId(resultSet.getInt("user_id"))
                .bookId(resultSet.getInt("book_id"))
                .published(resultSet.getBoolean("published"))
                .creationTime(resultSet.getTimestamp("creation_time").toLocalDateTime())
                .build();
    }
}
