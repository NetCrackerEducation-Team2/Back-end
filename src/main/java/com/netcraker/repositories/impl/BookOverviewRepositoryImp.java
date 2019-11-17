package com.netcraker.repositories.impl;

import com.netcraker.model.BookOverview;
import com.netcraker.repositories.BookOverviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
@RequiredArgsConstructor
public class BookOverviewRepositoryImp implements BookOverviewRepository {

    private final JdbcTemplate jdbcTemplate;
    //private final BookOverviewRowMapper bookOverviewRowMapper;
    @Value("${book_overviews.insert}")
    private String sqlInsert;
    @Value("${book_overviews.update}")
    private String sqlUpdate;

    @Override
    public List<BookOverview> getByBook(int bookId) {
        return null;
    }

    @Override
    public Optional<BookOverview> getPublishedByBook(int bookId) {
        return Optional.empty();
    }

    @Override
    public Optional<BookOverview> getById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<BookOverview> insert(BookOverview entity) {
        KeyHolder keyHolder;
        try {
            keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getDescription());
                ps.setInt(2, entity.getUserId());
                ps.setInt(3, entity.getBookId());
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            System.out.println("Book Overview::insert entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
        return getById((Integer) keyHolder.getKeys().get("book_overview_id"));
    }

    @Override
    public Optional<BookOverview> update(BookOverview entity) {
        try {
            jdbcTemplate.execute(Objects.requireNonNull(sqlUpdate), (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(1, entity.getDescription());
                ps.setInt(2, entity.getUserId());
                ps.setInt(3, entity.getBookId());
                ps.setInt(4, entity.getBookOverviewId());
                return ps.execute();
            });
            return getById(entity.getBookOverviewId());
        } catch (DataAccessException e) {
            System.out.println("Book Overview::update entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
