package com.netcraker.repositories.impl;

import com.netcraker.model.Entity;
import com.netcraker.model.annotations.EntityId;
import com.netcraker.model.annotations.GenericModel;
import com.netcraker.repositories.GenericRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
@RequiredArgsConstructor
@Slf4j
public abstract class GenericRepositoryImp<T extends Entity> implements GenericRepository<T>  {
    protected final JdbcTemplate jdbcTemplate;

    protected abstract RowMapper<T> getRowMapper();
    protected abstract String getSqlGetByIdQuery();
    protected abstract String getSqlInsertQuery();
    protected abstract String getSqlUpdateQuery();
    protected abstract String getSqlDeleteQuery();
    protected abstract String getSqlCountQuery();
    protected abstract int setParams(T entity, PreparedStatement ps, int firstParamIndex);

    @Override
    public Optional<T> getById(int id) {
        //String entityName = getEntityName(entity);
        //log.info("Trying to get from {} with id = {}", entityName, id);
        List<T> list =  jdbcTemplate.query(getSqlGetByIdQuery(), getRowMapper(), id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<T> insert(T entity) {
        log.info("Trying to insert {} to table {}", entity, getEntityName(entity));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(getSqlInsertQuery(), Statement.RETURN_GENERATED_KEYS);
            return setInsertQueryParams(entity, ps);
        }, keyHolder);
        return getById((Integer) keyHolder.getKeys().get(getEntityIdName(entity)));
    }

    @Override
    public Optional<T> update(T entity) {
        log.info("Trying to update {} in table", entity, getEntityName(entity));
        jdbcTemplate.execute(getSqlUpdateQuery(), (PreparedStatementCallback<Boolean>) ps -> setUpdateQueryParams(entity, ps).execute());
        return getById(entity.getId());
    }

    @Override
    public boolean delete(T entity) {
        String entityName = getEntityName(entity);
        log.info("Trying to delete from {} with id = {}", entityName, entity.getId());
        return jdbcTemplate.execute(getSqlDeleteQuery(), (PreparedStatement ps) -> {
            ps.setInt(1, entity.getId());
            return ps.execute();
        });
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(getSqlCountQuery(), int.class);
    }


    protected PreparedStatement setInsertQueryParams(T entity, PreparedStatement ps) {
        setParams(entity, ps, 1);
        return ps;
    }

    protected PreparedStatement setUpdateQueryParams(T entity, PreparedStatement ps) {
        try {
            ps.setInt(setParams(entity, ps, 1), entity.getId());
            return ps;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getEntityName(T entity) {
        String entityName = "";
        Class<T> classEntity = (Class<T>)entity.getClass();
        if(!classEntity.isAnnotationPresent(GenericModel.class)){
            log.error("no annotation @GenericModel in class {}", classEntity.getName());
        } else {
            entityName = classEntity.getAnnotation(GenericModel.class).value();
        }
        return entityName;
    }

    private String getEntityIdName(T entity) {
        String entityIdName = "";
        for (Field f: entity.getClass().getDeclaredFields()) {
            if(f.isAnnotationPresent(EntityId.class)) {
                entityIdName = f.getAnnotation(EntityId.class).value();
            }
        }
        return entityIdName;
    }

}
