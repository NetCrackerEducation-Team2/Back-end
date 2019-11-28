package com.netcraker.repositories.impl;

import com.netcraker.model.BookReview;
import com.netcraker.model.Page;
import com.netcraker.model.mapper.BookReviewRowMapper;
import com.netcraker.repositories.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class BookReviewRepositoryImp implements BookReviewRepository {

    private static final Logger logger = LoggerFactory.getLogger(BookReviewRepositoryImp.class);
    private final JdbcTemplate jdbcTemplate;

    @Value("${book_reviews.getById}")
    private String sqlGetById;
    @Value("${book_reviews.getAll}")
    private String sqlGetReviews;
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
    @Value("${book_reviews.countByUserIdBookId}")
    private String sqlCountByUserIdBookId;
    @Value("${book_reviews.publish}")
    private String sqlPublish;
    @Value("${book_reviews.unpublish}")
    private String sqlUnpublish;
    @Value("${book_reviews.count}")
    private String sqlGetBookReviewsCount;

    @Override
    public Optional<BookReview> getById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlGetById, new BookReviewRowMapper(), id));
        } catch (DataAccessException e) {
            logger.info("BookReview::getById id: " + id + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(sqlGetBookReviewsCount, int.class);
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
    public List<BookReview> getPage(int bookId, int pageSize, int offset) {
        return jdbcTemplate.query(sqlGetPage, new Object[]{bookId, pageSize, offset}, new BookReviewRowMapper());
    }

    @Override
    public Optional<BookReview> insert(BookReview entity) {
        KeyHolder keyHolder;

        try {
            keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, entity.getRating());
                ps.setString(2, entity.getDescription());
                ps.setInt(3, entity.getUserId());
                ps.setInt(4, entity.getBookId());
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            logger.info("BookReview::insert entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
        return getById((Integer) keyHolder.getKeys().get("book_review_id"));
    }

    @Override
    public List<BookReview> getBookReviews(int limit, int offset) {
        return jdbcTemplate.query(sqlGetReviews, new Object[]{limit, offset}, new BookReviewRowMapper());
    }

    @Override
    public Optional<BookReview> update(BookReview entity) {
        try {
            jdbcTemplate.execute(Objects.requireNonNull(sqlUpdate), (PreparedStatementCallback<Boolean>) ps -> {
                ps.setInt(1, entity.getRating());
                ps.setString(2, entity.getDescription());
                ps.setBoolean(3, entity.isPublished());
                ps.setInt(4, entity.getBookReviewId());
                return ps.execute();
            });
            return getById(entity.getBookReviewId());
        } catch (DataAccessException e) {
            logger.info("BookReview::update entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public int countByUserIdBookId(Integer userId, Integer bookId) {
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sqlCountByUserIdBookId, Integer.class, userId, bookId));
    }

    @Override
    public void publish(int id) {
        Object[] params = {id};
        jdbcTemplate.update(sqlPublish, params);

    }

    @Override
    public void unpublish(int id){
        Object[] params = {id};
        jdbcTemplate.update(sqlUnpublish, params);

    }
}
