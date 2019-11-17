package com.netcraker.repositories.impl;

import com.netcraker.model.Genre;
import com.netcraker.model.mapper.GenreRowMapper;
import com.netcraker.repositories.GenreRepository;
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
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
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
    public Optional<Genre> getById(int id) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlGetById, new GenreRowMapper()));
        } catch (DataAccessException e) {
            System.out.println("Genre::getById id: " + id + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Genre> insert(Genre entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getDescription());
                return ps;
            }, keyHolder);
        }catch (DataAccessException e){
            System.out.println("Genre::insert entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
        return getById((Integer) keyHolder.getKeys().get("genre_id"));
    }

    @Override
    public Optional<Genre> update(Genre entity) {
        try {
            jdbcTemplate.execute(sqlUpdate, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getDescription());
                ps.setInt(3, entity.getGenreId());
                return ps.execute();
            });
        }catch (DataAccessException e){
            System.out.println("Genre::update entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
        return getById(entity.getGenreId());
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(sqlDelete, id) == 1;
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