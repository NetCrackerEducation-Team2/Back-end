package com.netcraker.repositories.impl;

import com.netcraker.model.BookReview;
import com.netcraker.model.mapper.BookReviewRowMapper;
import com.netcraker.repositories.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class BookReviewRepositoryImpl implements BookReviewRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Environment environment;
    private final BookReviewRowMapper bookReviewRowMapper;


    @Override
    public Optional<BookReview> getById(int id) {
        final String query = environment.getProperty("book_reviews.getById");
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, bookReviewRowMapper, id));
        } catch (DataAccessException e) {
            System.out.println("BookReview::getById id: " + id + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<BookReview> insert(Optional<BookReview> entity) {
        final String query = environment.getProperty("book_reviews.insert");
        final BookReview bookReview = entity.orElse(BookReview.builder().description("").creationTime(LocalDateTime.now()).build());
        KeyHolder keyHolder;

        try {
            keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, bookReview.getRating());
                ps.setString(2, bookReview.getDescription());
                ps.setInt(3, bookReview.getUserId());
                ps.setInt(4, bookReview.getBookId());
                ps.setBoolean(5, bookReview.isPublished());
                ps.setDate(6, Date.valueOf(LocalDateTime.now().toLocalDate()));
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            System.out.println("BookReview::insert entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
        return getById((Integer) keyHolder.getKeys().get("book_review_id"));
    }

    @Override
    public Optional<BookReview> update(Optional<BookReview> entity) {
        final String query = environment.getProperty("book_reviews.update");
        final BookReview bookReview = entity.orElse(BookReview.builder().description("").creationTime(LocalDateTime.now()).build());

        try {
            jdbcTemplate.execute(Objects.requireNonNull(query), (PreparedStatementCallback<Boolean>) ps -> {
                ps.setInt(1, bookReview.getRating());
                ps.setString(2, bookReview.getDescription());
                ps.setBoolean(3, bookReview.isPublished());
                ps.setInt(4, bookReview.getBookReviewId());
                return ps.execute();
            });
            return getById(bookReview.getBookReviewId());
        } catch (DataAccessException e) {
            System.out.println("BookReview::update entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(int id) {
        final String query = environment.getProperty("book_reviews.delete");
        return jdbcTemplate.update(query, id) == 1;
    }

    @Override
    public double getAverageRating(int bookId) {
        final String query = environment.getProperty("book_reviews.avgRating");
        return Objects.requireNonNull(jdbcTemplate.queryForObject(query, Double.class, bookId));
    }

    @Override
    public List<BookReview> getPage(int bookId, int page, int count) {
        final String query = environment.getProperty("book_reviews.getPage");
        return jdbcTemplate.query(query, new Object[]{bookId, page, count}, bookReviewRowMapper);
    }
}
