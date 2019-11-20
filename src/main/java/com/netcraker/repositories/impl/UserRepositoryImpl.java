package com.netcraker.repositories.impl;

import com.netcraker.exceptions.FindException;
import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.User;
import com.netcraker.model.mapper.UserRowMapper;
import com.netcraker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PropertySource("classpath:sqlQueries.properties")
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${user.findByEmail}")
    private String sqlFindByEmail;

    @Value("${user.activate}")
    private String sqlActivate;

    @Value("${user.getById}")
    private String sqlGetById;

    @Value("${user.insert}")
    private String sqlInsert;

    @Value("${user.update}")
    private String sqlUpdate;

    @Value("${user.delete}")
    private String sqlDelete;

    @Override
    public Optional<User> findByEmail(String email) {
        Object[] params = {email};
        List<User> users = jdbcTemplate.query(sqlFindByEmail, params, new UserRowMapper());
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public Optional<User> activateUser(String email) {
        Optional<User> entity = findByEmail(email);
        if (!entity.isPresent())
            throw new FindException("User is not activated! User is not found.");

        User user = entity.get();
        Object[] params = {true, user.getUserId(), user.getEmail(), user.getPassword()};
        int changedRowsCount = jdbcTemplate.update(sqlActivate, params);

        if (changedRowsCount == 0)
            throw new UpdateException("User is not activated! User is not found.");
        if (changedRowsCount > 1)
            throw new UpdateException("User is not activated! Multiple update! Only one user can be changed.");

        return entity;
    }

    @Override
    public Optional<User> getById(int id) {
        Object[] params = {id};
        List<User> users = jdbcTemplate.query(sqlGetById, params, new UserRowMapper());
        return Optional.ofNullable(users.get(0));
    }

    @Override
    public Optional<User> insert(User entity) {
        System.out.println("trying to add user to db: " + entity);
        Object[] params = {entity.getFullName(), entity.getPassword(), entity.getEmail(),
                new Timestamp(System.currentTimeMillis()), entity.getEnabled(), entity.getPhotoPath()};
        jdbcTemplate.update(sqlInsert, params);

        return findByEmail(entity.getEmail());
    }

    @Override
    public Optional<User> update(User entity) {

        Object[] params = {entity.getPassword(),
                entity.getEmail(),
                entity.getEnabled(),
                entity.getPhotoPath(),
                entity.getFullName(),
                entity.getUserId()};

        int changedRowsCount = jdbcTemplate.update(sqlUpdate, params);

        if (changedRowsCount == 0)
            throw new UpdateException("User is not found!");
        if (changedRowsCount > 1)
            throw new UpdateException("Multiple update! Only one user can be changed!");

        return getById(entity.getUserId());
    }

    @Override
    public boolean delete(int id) {
        Object[] params = {id};
        return jdbcTemplate.update(sqlDelete, params) == 1;
    }
}
