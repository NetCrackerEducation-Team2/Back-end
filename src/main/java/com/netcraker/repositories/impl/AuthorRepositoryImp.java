package com.netcraker.repositories.impl;

import com.netcraker.controllers.AnnouncementController;
import com.netcraker.model.Author;
import com.netcraker.model.mapper.AuthorRowMapper;
import com.netcraker.repositories.AuthorRepository;
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
import java.util.Optional;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class AuthorRepositoryImp implements AuthorRepository {

    private static final Logger logger = LoggerFactory.getLogger(AuthorRepositoryImp.class);
    private final JdbcTemplate jdbcTemplate;

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
    @Value("${authors.searchByFullNameContains}")
    private String sqlSearchByNameContains;
    @Value("${authors.searchPartByFullNameContains}")
    private String getSqlSearchPartByNameContains;

    @Override
    public Optional<Author> getById(int id) {
        List<Author> authors = jdbcTemplate.query(sqlGetById, new AuthorRowMapper(), id);
        return authors.isEmpty() ? Optional.empty() : Optional.of(authors.get(0));
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
            logger.info("Author::insert entity: " + entity + ". Stack trace: ");
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
            logger.info("Author::update entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
        return getById(entity.getAuthorId());
    }

    @Override
    public boolean delete(int id) {
        try {
            return jdbcTemplate.update(sqlDelete, id) == 1;
        }catch (DataAccessException e){
            logger.info("Author::delete entityId: " + id + ". Stack trace: ");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Author> getAll() {
        return jdbcTemplate.query(sqlGetAll, new AuthorRowMapper());
    }

    @Override
    public List<Author> getByBook(int bookId) {
        return jdbcTemplate.query(sqlGetByBook, new AuthorRowMapper(), bookId);
    }

    @Override
    public List<Author> searchByNameContains(String authorFullNameContains) {
        return jdbcTemplate.query(sqlSearchByNameContains, new AuthorRowMapper(), "%" + authorFullNameContains.trim() + "%");
    }

    @Override
    public List<Author> searchByNameContains(String authorFullNameContains, int offset, int size) {
        return jdbcTemplate.query(getSqlSearchPartByNameContains, new AuthorRowMapper(), authorFullNameContains.trim() + "%", size, offset);
    }
}