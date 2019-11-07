package com.netcraker.model.mapper;

import com.netcraker.model.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreRowMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        Genre genre = Genre.builder()
                .genreId(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .build();
        return genre;
    }
}
