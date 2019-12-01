package com.netcraker.repositories.impl;

import com.netcraker.model.Book;
import com.netcraker.model.mapper.BookRowMapper;
import com.netcraker.repositories.BooksRecommendationsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class BooksRecommendationsRepositoryImp implements BooksRecommendationsRepository {

    private static final Logger logger = LoggerFactory.getLogger(BooksRecommendationsRepositoryImp.class);
    private final JdbcTemplate jdbcTemplate;

    @Value("${booksRecommendations.count}")
    private String sqlCount;
    @Value("${booksRecommendations.get}")
    private String sqlGet;
    @Value("${booksRecommendations.getLatestUpdateTime}")
    private String sqlGetLatestUpdateTime;
    @Value("${booksRecommendations.insert}")
    private String sqlInsert;
    @Value("${booksRecommendations.clear}")
    private String sqlClear;

    @Override
    public int count(int userId) {
        return jdbcTemplate.queryForObject(sqlCount, new Object[]{userId}, int.class);
    }

    @Override
    public List<Book> get(int userId) {
        return jdbcTemplate.query(sqlGet, new Object[]{userId}, new BookRowMapper());
    }

    @Override
    public LocalDateTime getLatestUpdateTime(int userId) {
        return jdbcTemplate.queryForObject(sqlGetLatestUpdateTime, new Object[]{userId}, LocalDateTime.class);
    }

    @Override
    public void insert(int userId, List<Book> books) {
        this.jdbcTemplate.batchUpdate(sqlInsert, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, userId);
                ps.setInt(2, books.get(i).getBookId());
            }

            public int getBatchSize() {
                return books.size();
            }
        });
    }

    @Override
    public void clear(int userId) {
        jdbcTemplate.execute(sqlClear, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setInt(1, userId);
            return ps.execute();
        });
    }
}
