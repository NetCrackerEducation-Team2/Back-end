package com.netcraker.repositories;

import com.netcraker.model.User;
import com.netcraker.model.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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

    @Value("${user.update.enable}")
    private String sqlUpdateUserFieldEnable;


    public User createUser(User user) {
        Object[] params = { user.getFullName(), user.getPassword(), user.getEmail(),
                    new Timestamp(System.currentTimeMillis()), false, user.getPhotoPath()};
        jdbcTemplate.update(sqlCreateUser, params);
        return user;
    }
    public void updateUser(User user) {
        Object[] params = {user.isEnabled(), user.getUserId(),user.getEmail(), user.getPassword()};
        int changedRowsCount = jdbcTemplate.update(sqlUpdateUserFieldEnable, params);
//        if (changedRowsCount == 0)
//            //throw new FailedToUpdateUserException("User is not found!");
//        if (changedRowsCount > 1)
//            throw new FailedToUpdateUserException("Multiple update! Only one user can be changed!");
    }

    public User findByEmail(String email) {
        Object[] param = {email};
        return jdbcTemplate.queryForObject(sqlSelectUserEmail, param, new UserRowMapper());
    }

    public User findByUserId(int userId) {
        return jdbcTemplate.queryForObject(sqlSelectUserId, new Object[]{userId}, new UserRowMapper());
    }
}