package com.netcraker.repositories;

import com.netcraker.model.Author;
import com.netcraker.model.mapper.AuthorRowMapper;
import io.jsonwebtoken.lang.Assert;
import lombok.NonNull;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorRepositoryImp implements AuthorRepository {

    private final @NonNull JdbcTemplate jdbcTemplate;

    @Value("${authors.getById}")
    private String sqlGetById;
    @Value("${authors.insert}")
    private String sqlInsert;
    @Value("${authors.update}")
    private String sqlUpdate;
    @Value("${authors.delete}")
    private String sqlDelete;
    @Value("${authors.getByBook}")
    private String sqlGetByBook;

    @Override
    public Author getById(int id) {
        return jdbcTemplate.queryForObject(sqlGetById, new AuthorRowMapper());
    }

    @Override
    public Author insert(Author entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getFullName());
            ps.setString(2, entity.getDescription());
            return ps;
        }, keyHolder);
        return getById((Integer) keyHolder.getKeys().get("author_id"));
    }

    @Override
    public Author update(Author entity) {
        jdbcTemplate.execute(sqlUpdate, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setString(1, entity.getFullName());
            ps.setString(2, entity.getDescription());
            ps.setInt(3, entity.getAuthorId());
            return ps.execute();
        });
        return getById(entity.getAuthorId());
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.execute(sqlDelete, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setInt(1, id);
            return ps.execute();
        });
    }

    @Override
    public List<Author> getByBook(int bookId) {
        return jdbcTemplate.query(sqlGetByBook, new AuthorRowMapper(), bookId);
    }
}