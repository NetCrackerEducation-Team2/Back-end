package com.netcraker.repositories.impl;

import com.netcraker.model.Author;
import com.netcraker.model.mapper.AuthorRowMapper;
import com.netcraker.repositories.AuthorRepository;
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
import java.util.Optional;

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
    @Value("${authors.getAll}")
    private String sqlGetAll;
    @Value("${authors.getByBook}")
    private String sqlGetByBook;

    @Override
    public Optional<Author> getById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlGetById, new AuthorRowMapper()));
        }catch (DataAccessException e) {
            System.out.println("Author::getById id: " + id + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Author> insert(Author entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getFullName());
                ps.setString(2, entity.getDescription());
                return ps;
            }, keyHolder);
        }catch (DataAccessException e){
            System.out.println("Author::insert entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
        return getById((Integer) keyHolder.getKeys().get("author_id"));
    }

    @Override
    public Optional<Author> update(Author entity) {
        try {
            jdbcTemplate.execute(sqlUpdate, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(1, entity.getFullName());
                ps.setString(2, entity.getDescription());
                ps.setInt(3, entity.getAuthorId());
                return ps.execute();
            });
        }catch (DataAccessException e){
            System.out.println("Author::update entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
        return getById(entity.getAuthorId());
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(sqlDelete, id) == 1;

    }

    @Override
    public List<Author> getAll() {
        return jdbcTemplate.query(sqlGetAll, new AuthorRowMapper());
    }

    @Override
    public List<Author> getByBook(int bookId) {
        return jdbcTemplate.query(sqlGetByBook, new AuthorRowMapper(), bookId);
    }
}