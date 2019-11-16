package com.netcraker.repositories.impl;

import com.netcraker.model.Genre;
import com.netcraker.model.mapper.GenreRowMapper;
import com.netcraker.repositories.GenreRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenreRepositoryImp implements GenreRepository {

    private final @NonNull JdbcTemplate jdbcTemplate;

    @Value("${genres.getById}")
    private String sqlGetById;
    @Value("${genres.insert}")
    private String sqlInsert;
    @Value("${genres.update}")
    private String sqlUpdate;
    @Value("${genres.delete}")
    private String sqlDelete;
    @Value("${genres.getAll}")
    private String sqlGetAll;
    @Value("${genres.getByBook}")
    private String sqlGetByBook;

    @Override
    public Genre getById(int id) {
        return jdbcTemplate.queryForObject(sqlGetById, new GenreRowMapper());
    }

    @Override
    public Genre insert(Genre entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            return ps;
        }, keyHolder);
        return getById((Integer) keyHolder.getKeys().get("genre_id"));
    }

    @Override
    public Genre update(Genre entity) {
        jdbcTemplate.execute(sqlUpdate, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setInt(3, entity.getGenreId());
            return ps.execute();
        });
        return getById(entity.getGenreId());
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.execute(sqlDelete, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setInt(1, id);
            return ps.execute();
        });
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(sqlGetAll, new GenreRowMapper());
    }

    @Override
    public List<Genre> getByBook(int bookId) {
        return jdbcTemplate.query(sqlGetByBook, new GenreRowMapper(), bookId);
    }
}