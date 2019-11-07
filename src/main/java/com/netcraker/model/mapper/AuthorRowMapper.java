package com.netcraker.model.mapper;

import com.netcraker.model.Author;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorRowMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        Author author = Author.builder()
                .authorId(resultSet.getInt("authorId"))
                .fullName(resultSet.getString("full_name"))
                .description(resultSet.getString("description"))
                .build();
        return author;
    }
}
