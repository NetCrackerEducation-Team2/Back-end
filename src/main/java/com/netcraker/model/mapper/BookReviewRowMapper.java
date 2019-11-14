package com.netcraker.model.mapper;

import com.netcraker.model.BookReview;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BookReviewRowMapper implements RowMapper<BookReview> {

    @Override
    public BookReview mapRow(ResultSet resultSet, int i) throws SQLException {
        return BookReview.builder()
                .bookReviewId(resultSet.getInt("book_review_id"))
                .rating(resultSet.getInt("rating"))
                .description(resultSet.getString("description"))
                .userId(resultSet.getInt("user_id"))
                .bookId(resultSet.getInt("book_id"))
                .published(resultSet.getBoolean("published"))
                .creationTime(resultSet.getTimestamp("creation_time").toLocalDateTime())
                .build();
    }
}
