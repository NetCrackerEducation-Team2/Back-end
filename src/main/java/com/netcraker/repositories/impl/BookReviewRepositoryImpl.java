package com.netcraker.repositories.impl;

import com.netcraker.model.BookReview;
import com.netcraker.model.mapper.BookReviewRowMapper;
import com.netcraker.repositories.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final BookReviewRowMapper bookReviewRowMapper;

    @Value("${book_reviews.getById}")
    private String sqlGetById;
    @Value("${book_reviews.insert}")
    private String sqlInsert;
    @Value("${book_reviews.update}")
    private String sqlUpdate;
    @Value("${book_reviews.delete}")
    private String sqlDelete;
    @Value("${book_reviews.avgRating}")
    private String sqlAvgRating;
    @Value("${book_reviews.getPage}")
    private String sqlGetPage;


    @Override
    public Optional<BookReview> getById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlGetById, bookReviewRowMapper, id));
        } catch (DataAccessException e) {
            System.out.println("BookReview::getById id: " + id + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<BookReview> insert(Optional<BookReview> entity) {
        final BookReview bookReview = entity.orElse(BookReview.builder().description("").creationTime(LocalDateTime.now()).build());
        KeyHolder keyHolder;

        try {
            keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, bookReview.getRating());
                ps.setString(2, bookReview.getDescription());
                ps.setInt(3, bookReview.getUserId());
                ps.setInt(4, bookReview.getBookId());
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
        final BookReview bookReview = entity.orElse(BookReview.builder().description("").creationTime(LocalDateTime.now()).build());

        try {
            jdbcTemplate.execute(Objects.requireNonNull(sqlUpdate), (PreparedStatementCallback<Boolean>) ps -> {
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
        return jdbcTemplate.update(sqlDelete, id) == 1;
    }

    @Override
    public double getAverageRating(int bookId) {
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sqlAvgRating, Double.class, bookId));
    }

    @Override
    public List<BookReview> getPage(int bookId, int page, int count) {
        return jdbcTemplate.query(sqlGetPage, new Object[]{bookId, page, count}, bookReviewRowMapper);
    }
}
