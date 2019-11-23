package com.netcraker.model.mapper;

import com.netcraker.model.ReviewComment;
import org.springframework.jdbc.core.RowMapper;

import java.security.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;


public class ReviewCommentRowMapper implements RowMapper<ReviewComment> {
    @Override
    public ReviewComment mapRow(ResultSet resultSet, int i) throws SQLException {
        return ReviewComment.builder()
                .commentId(resultSet.getInt("comment_id"))
                .authorId(resultSet.getInt("author_id"))
                .bookReviewId(resultSet.getInt("book_review_id"))
                .content(resultSet.getString("content"))
                .creationTime(resultSet.getTimestamp("creation_time").toLocalDateTime())
                .build();
    }
}
