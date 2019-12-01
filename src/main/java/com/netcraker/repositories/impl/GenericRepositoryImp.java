package com.netcraker.repositories.impl;

import com.netcraker.repositories.GenericRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
@RequiredArgsConstructor
public class GenericRepositoryImp<T, RM extends RowMapper<T>> implements GenericRepository<T, RM> {
    private final JdbcTemplate jdbcTemplate;
    //@Qualifier(value = "notificationObjectRowMapper")
    //private final RM rowMapper;

    /*@Autowired
    public <T> GenericRepositoryImp(RowMapper<T> mapper) {
        jdbcTemplate = new JdbcTemplate();
        rowMapper = mapper;
    }*/

    @Override
    public Optional<T> getById(RM rowMapper, String sql, int id) {
        List<T> list =  jdbcTemplate.query(sql, rowMapper, new Object[] {id});
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public Optional<T> insert(Object[] params, String sql, String idName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i+1, params[i]);
            }
            return ps;
        }, keyHolder);

        //return getById(rowMapper, (Integer) keyHolder.getKeys().get(idName));
        return Optional.empty();
    }

}
