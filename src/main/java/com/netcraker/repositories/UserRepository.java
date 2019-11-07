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
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PropertySource("classpath:sqlQueries.properties")
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.queries.user.create}")
    private String sqlCreateUser;

    @Value("${spring.queries.user.findByUserId}")
    private String sqlSelectUserId;

    @Value("${spring.queries.user.findByUsername}")
    private String sqlSelectUserEmail;

    @Value("${spring.queries.user.update}")
    private String sqlUpdateUser;

    @Value("${spring.queries.user.delete}")
    private String sqlDeleteUser;

    @Value("${spring.queries.user.getByFilter}")
    private String sqlGetUsersByFilter;

    @Value("${spring.queries.user.updateByFilter}")
    private String sqlUpdateUsersByFilter;

    @Value("${spring.queries.user.deleteByFilter}")
    private String sqlDeleteUsersByFilter;


    public User createUser(User user) {
        Object[] params = { user.getFull_name(), user.getPassword(), user.getEmail(),
                            new Timestamp(System.currentTimeMillis()), true, user.getPhotoPath() };
        jdbcTemplate.update(sqlCreateUser, params);
        return user;
    }

    public User findByUsername(String username) {
        Object[] params = { username };
        return jdbcTemplate.queryForObject(sqlSelectUserEmail, params, new UserRowMapper());
    }

    public User findByUserId(int userId) {
        Object[] params = { userId };
        return jdbcTemplate.queryForObject(sqlSelectUserId, params, new UserRowMapper());
    }

    public void updateUser(User user) throws SQLDataException {
        Object[] params = { user.getFull_name(), user.getEmail(), user.getPassword(),
                            user.getCreatedAt(), user.getEnabled(), user.getPhotoPath(), user.getUserId() };
        int changedRowsCount = jdbcTemplate.update(sqlUpdateUser, params);
        if (changedRowsCount != 1)
            throw new SQLDataException();
    }

    public void deleteUser(User user) throws SQLDataException {
        Object[] params = { user.getUserId(), user.getFull_name(), user.getEmail(), user.getPassword(),
                            user.getCreatedAt(), user.getEnabled(), user.getPhotoPath() };
        int changedRowsCount = jdbcTemplate.update(sqlDeleteUser, params);
        if (changedRowsCount != 1)
            throw new SQLDataException();
    }


    public List<User> getUsersByFilter(User userFilter, int paginationPosition, int paginationCount) {
        Object[] params = {
                userFilter.getFull_name(),  userFilter.getFull_name()  == null,
                userFilter.getEmail(),      userFilter.getEmail()      == null,
                userFilter.getPassword(),   userFilter.getPassword()   == null,
                userFilter.getCreatedAt(),  userFilter.getCreatedAt()  == null,
                userFilter.getEnabled(),    userFilter.getEnabled()    == null,
                userFilter.getPhotoPath(),  userFilter.getPhotoPath()  == null,
                paginationCount,
                paginationPosition
        };
        return jdbcTemplate.query(sqlGetUsersByFilter, params, new UserRowMapper());
    }

    public int updateUsersByFilter(User userFilter, User userNewData) {
        Object[] params = {
                userNewData.getFull_name()  == null, userNewData.getFull_name(),
                userNewData.getEmail()      == null, userNewData.getEmail(),
                userNewData.getPassword()   == null, userNewData.getPassword(),
                userNewData.getCreatedAt()  == null, userNewData.getCreatedAt(),
                userNewData.getEnabled()    == null, userNewData.getEnabled(),
                userNewData.getPhotoPath()  == null, userNewData.getPhotoPath(),

                userFilter.getFull_name(),  userFilter.getFull_name()  == null,
                userFilter.getEmail(),      userFilter.getEmail()      == null,
                userFilter.getPassword(),   userFilter.getPassword()   == null,
                userFilter.getCreatedAt(),  userFilter.getCreatedAt()  == null,
                userFilter.getEnabled(),    userFilter.getEnabled()    == null,
                userFilter.getPhotoPath(),  userFilter.getPhotoPath()  == null,
        };
        return jdbcTemplate.update(sqlUpdateUsersByFilter, params);
    }

    public int deleteUsersByFilter(User userFilter) {
        Object[] params = {
                userFilter.getFull_name(),  userFilter.getFull_name()  == null,
                userFilter.getEmail(),      userFilter.getEmail()      == null,
                userFilter.getPassword(),   userFilter.getPassword()   == null,
                userFilter.getCreatedAt(),  userFilter.getCreatedAt()  == null,
                userFilter.getEnabled(),    userFilter.getEnabled()    == null,
                userFilter.getPhotoPath(),  userFilter.getPhotoPath()  == null,
        };
        return jdbcTemplate.update(sqlDeleteUsersByFilter, params);
    }
}