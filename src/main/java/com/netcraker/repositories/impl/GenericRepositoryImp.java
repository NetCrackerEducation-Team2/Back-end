package com.netcraker.repositories.impl;

import com.netcraker.model.Entity;
import com.netcraker.repositories.GenericRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
@RequiredArgsConstructor
public abstract class GenericRepositoryImp<T extends Entity> implements GenericRepository<T> {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(GenericRepositoryImp.class);

    protected abstract RowMapper<T> getRowMapper();
    protected abstract String getSqlGetByIdQuery();
    protected abstract String getSqlInsertQuery();
    protected abstract PreparedStatement setInsertQueryParams(T entity, PreparedStatement ps);
    protected abstract String getSqlUpdateQuery();
    protected abstract PreparedStatement setUpdateQueryParams(T entity, PreparedStatement ps);
    protected abstract String getIdColumnName();
    protected abstract String getSqlDeleteQuery();
    @Override
    public Optional<T> getById(int id) {
        List<T> list =  jdbcTemplate.query(getSqlGetByIdQuery(), getRowMapper(), id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<T> insert(T entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(getSqlInsertQuery(), Statement.RETURN_GENERATED_KEYS);
            return setInsertQueryParams(entity, ps);
        }, keyHolder);
        return getById((Integer) keyHolder.getKeys().get(getIdColumnName()));
    }

    @Override
    public Optional<T> update(T entity) {
        jdbcTemplate.execute(getSqlUpdateQuery(), (PreparedStatementCallback<Boolean>) ps -> {
            return setUpdateQueryParams(entity, ps).execute();
        });
        return getById(entity.getId());
    }

    @Override
    public boolean delete(T entity) {
        return jdbcTemplate.execute(getSqlDeleteQuery(), (PreparedStatement ps) -> {
            ps.setInt(1, entity.getId());
            return ps.execute();
        });
    }
}
