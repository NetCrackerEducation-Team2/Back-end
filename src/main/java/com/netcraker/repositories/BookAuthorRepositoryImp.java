package com.netcraker.repositories;

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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookAuthorRepositoryImp implements BookAuthorRepository {

    private final @NonNull JdbcTemplate jdbcTemplate;

    @Value("${books_authors.insert}")
    private String sqlInsert;
    @Value("${books_authors.delete}")
    private String sqlDelete;

    @Override
    public boolean insert(int bookId, int authorId) {
        return jdbcTemplate.execute(sqlInsert, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setInt(1, bookId);
            ps.setInt(2, authorId);
            return ps.execute();
        });
    }

    @Override
    public boolean delete(int bookId, int authorId) {
        return jdbcTemplate.execute(sqlDelete, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setInt(1, bookId);
            ps.setInt(2, authorId);
            return ps.execute();
        });
    }
}
