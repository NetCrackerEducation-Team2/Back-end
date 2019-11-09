package com.netcraker.repositories;

import com.netcraker.model.User;
import com.netcraker.model.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;


@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PropertySource("classpath:sqlQueries.properties")
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.queries.create.user}")
    private String sqlCreateUser;

    @Value("${spring.queries.find.user.id}")
    private String sqlSelectUserId;

    @Value("${spring.queries.find.user.email}")
    private String sqlSelectUserEmail;


    public User createUser(User user) {
        jdbcTemplate.update(sqlCreateUser,  user.getFullName(), user.getPassword(), user.getEmail(),
                                        new Timestamp(System.currentTimeMillis()), true, user.getPhotoPath());
        return user;
    }

    public User findByEmail(String email) {
        return jdbcTemplate.queryForObject(sqlSelectUserEmail, new Object[]{email}, new UserRowMapper());
    }

    public User findByUserId(int userId) {
        return jdbcTemplate.queryForObject(sqlSelectUserId, new Object[]{userId}, new UserRowMapper());
    }
}