package com.netcraker.repositories;

import com.netcraker.model.Genre;
import com.netcraker.model.mapper.GenreRowMapper;
import io.jsonwebtoken.lang.Assert;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:sqlQueries.properties")
public class GenreRepositoryImp implements GenreRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreRepositoryImp(JdbcTemplate jdbcTemplate) {
        Assert.notNull(jdbcTemplate, "JdbcTemplate shouldn't be null");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Value("${genres.getById}")
    private String sqlGetById;
    @Value("${genres.insert}")
    private String sqlInsert;
    @Value("${genres.update}")
    private String sqlUpdate;
    @Value("${genres.delete}")
    private String sqlDelete;
    @Value("${genres.getByBook}")
    private String sqlGetByBook;

    @Override
    public Genre getById(int id) {
        return jdbcTemplate.queryForObject(sqlGetById, new GenreRowMapper());
    }

    @Override
    public boolean insert(Genre entity) {
        return jdbcTemplate.execute(sqlInsert, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            return ps.execute();
        });
    }

    @Override
    public boolean update(Genre entity) {
        return jdbcTemplate.execute(sqlUpdate, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setInt(3, entity.getGenreId());
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
    public List<Genre> getByBook(int bookId) {
        return jdbcTemplate.query(sqlGetByBook, new GenreRowMapper(), bookId);
    }
}