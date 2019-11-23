package com.netcraker.repositories.impl;

import com.netcraker.model.BookReview;
import com.netcraker.model.ReviewComment;
import com.netcraker.model.mapper.BookReviewRowMapper;
import com.netcraker.model.mapper.ReviewCommentRowMapper;
import com.netcraker.repositories.ReviewCommentRepository;
import com.netcraker.services.PageService;
import lombok.RequiredArgsConstructor;
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
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class ReviewCommentRepositoryImp implements ReviewCommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${review_comments.getById}")
    private String sqlGetById;
    @Value("${review_comments.insert}")
    private String sqlInsert;
    @Value("${review_comments.update}")
    private String sqlUpdate;
    @Value("${review_comments.delete}")
    private String sqlDelete;
    @Value("${review_comments.getPage}")
    private String sqlGetPage;
    @Value("${review_comments.countByBookReviewId}")
    private String sqlCountByBookReviewId;

    @Override
    public Optional<ReviewComment> getById(int id) {
        final List<ReviewComment> comment = jdbcTemplate.query(sqlGetById, new ReviewCommentRowMapper(), id);
        return Optional.ofNullable(comment.isEmpty() ? null : comment.get(0));
    }

    @Override
    public Optional<ReviewComment> insert(ReviewComment entity) {
        KeyHolder keyHolder;

        try {
            keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, entity.getAuthorId());
                ps.setInt(2, entity.getBookReviewId());
                ps.setString(3, entity.getContent());
                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            System.out.println("ReviewComment::insert entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
        return getById((Integer) keyHolder.getKeys().get("comment_id"));
    }

    @Override
    public Optional<ReviewComment> update(ReviewComment entity) {
        try {
            jdbcTemplate.execute(Objects.requireNonNull(sqlUpdate), (PreparedStatementCallback<Boolean>) ps -> {
                ps.setInt(1, entity.getAuthorId());
                ps.setInt(2, entity.getBookReviewId());
                ps.setString(3, entity.getContent());
                ps.setInt(4, entity.getCommentId());
                return ps.execute();
            });
            return getById(entity.getCommentId());
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
    public List<ReviewComment> getPage(int bookReviewId, int pageSize, int offset) {
        return jdbcTemplate.query(sqlGetPage, new Object[]{bookReviewId, pageSize, offset}, new ReviewCommentRowMapper());
    }

    @Override
    public int countByBookReviewId(int commentId) {
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sqlCountByBookReviewId, Integer.class, commentId));
    }
}
