package com.netcraker.repositories.impl;

import com.netcraker.model.annotations.EntityId;
import com.netcraker.model.annotations.GenericModel;
import com.netcraker.repositories.GenericRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
@RequiredArgsConstructor
public class GenericRepositoryImp<T, RM extends RowMapper<T>> implements GenericRepository<T, RM> {
    private final JdbcTemplate jdbcTemplate;
    private final Environment environment;
    private static final Logger logger = LoggerFactory.getLogger(GenericRepositoryImp.class);

    @Override
    public Optional<T> getById(Class<T> entity, RM rowMapper, int id) {
        String entityName = getEntityName(entity);
        logger.info("Trying to get from " + entityName + " with id = " + id);
        String sqlGetById = environment.getProperty(entityName + ".getById");
        List<T> list =  jdbcTemplate.query(sqlGetById, rowMapper, new Object[] {id});
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<T> insert(T entity, RM rowMapper, Object[] params) {
        Class<T> classObj = (Class<T>)entity.getClass();
        String entityName = getEntityName(classObj);
        logger.info("Trying to insert " + entity + " to table " + entityName);
        String entityIdName = getEntityIdName(classObj);
        String sqlInsert = environment.getProperty(entityName + ".insert");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i+1, params[i]);
            }
            return ps;
        }, keyHolder);

        return getById((Class<T>)entity.getClass(), rowMapper, (Integer) keyHolder.getKeys().get(entityIdName));
    }

    @Override
    public Optional<T> update(T entity, RM rowMapper, Object[] params, int id) {
        String entityName = getEntityName((Class<T>)entity.getClass());
        String sqlUpdate = environment.getProperty(entityName + ".update");
        logger.info("Trying to update " + entity);
        jdbcTemplate.execute(Objects.requireNonNull(sqlUpdate), (PreparedStatementCallback<Boolean>) ps -> {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i+1, params[i]);
            }
            return ps.execute();
        });
        return getById((Class<T>)entity.getClass(), rowMapper, id);
    }

    @Override
    public boolean delete(String entityName, int id) {
        String sqlDelete = environment.getProperty(entityName + ".delete");
        logger.info("Trying to delete " + id);
        return jdbcTemplate.execute(sqlDelete, (PreparedStatement ps) -> {
            ps.setInt(1, id);
            return ps.execute();
        });
    }

    private String getEntityName(Class<T> entity) {
        String entityName = "";
        if(!entity.isAnnotationPresent(GenericModel.class)){
            logger.error("no annotation @GenericModel in class " + entity.getName());
        } else {
            entityName = entity.getAnnotation(GenericModel.class).value();
        }
        return entityName;
    }

    private String getEntityIdName(Class<T> entity) {
        String entityIdName = "";
        for (Field f: entity.getDeclaredFields()) {
            if(f.isAnnotationPresent(EntityId.class)) {
                entityIdName = f.getAnnotation(EntityId.class).value();
            }
        }
        return entityIdName;
    }

}
