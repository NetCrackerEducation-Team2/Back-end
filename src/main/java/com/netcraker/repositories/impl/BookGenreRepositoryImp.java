package com.netcraker.repositories.impl;

import com.netcraker.repositories.BookGenreRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class BookGenreRepositoryImp implements BookGenreRepository {


    private final JdbcTemplate jdbcTemplate;

    @Value("${books_genres.insert}")
    private String sqlInsert;
    @Value("${books_genres.delete}")
    private String sqlDelete;

    @Override
    public boolean insert(int bookId, int genreId) {
        return jdbcTemplate.execute(sqlInsert, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setInt(1, bookId);
            ps.setInt(2, genreId);
            return ps.execute();
        });
    }

    @Override
    public boolean delete(int bookId, int genreId) {
        return jdbcTemplate.execute(sqlDelete, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setInt(1, bookId);
            ps.setInt(2, genreId);
            return ps.execute();
        });
    }
}
