package com.netcraker.model.mapper;

import com.netcraker.model.UserBook;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserBookRowMapper implements RowMapper<UserBook> {
    @Override
    public UserBook mapRow(ResultSet resultSet, int i) throws SQLException {
        return UserBook.builder()
                .userBookId(resultSet.getInt("users_books_id"))
                .bookId(resultSet.getInt("book_id"))
                .userId(resultSet.getInt("user_id"))
                .favoriteMark(resultSet.getBoolean("favorite"))
                .readMark(resultSet.getBoolean("read"))
                .creationTime(resultSet.getTimestamp("creation_time").toLocalDateTime())
                .build();
    }
}
