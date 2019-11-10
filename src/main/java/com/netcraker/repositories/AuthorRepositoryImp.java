package com.netcraker.repositories;

import com.netcraker.model.Author;
import com.netcraker.model.mapper.AuthorRowMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@PropertySource("classpath:sqlQueries.properties")
public class AuthorRepositoryImp implements AuthorRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorRepositoryImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
    public boolean insert(Author entity) {
        return jdbcTemplate.execute(sqlInsert, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setString(1, entity.getFullName());
            ps.setString(2, entity.getDescription());
            return ps.execute();
        });
    }

    @Override
    public boolean update(Author entity) {
        return jdbcTemplate.execute(sqlUpdate, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setString(1, entity.getFullName());
            ps.setString(2, entity.getDescription());
            ps.setInt(3, entity.getAuthorId());
            return ps.execute();
        });
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
