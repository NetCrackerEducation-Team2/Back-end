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

    @Value("${user.create}")
    private String sqlCreateUser;

    @Value("${user.findByUserId}")
    private String sqlSelectUserId;

    @Value("${user.findByUsername}")
    private String sqlSelectUserEmail;

    @Value("${user.update}")
    private String sqlUpdateUser;

    @Value("${user.delete}")
    private String sqlDeleteUser;

    @Value("${user.getByFilter}")
    private String sqlGetUsersByFilter;

    @Value("${user.updateByFilter}")
    private String sqlUpdateUsersByFilter;

    @Value("${user.deleteByFilter}")
    private String sqlDeleteUsersByFilter;


    public User createUser(User user) {
        Object[] params = { user.getFullName(), user.getPassword(), user.getEmail(),
                            new Timestamp(System.currentTimeMillis()), true, user.getPhotoPath() };
        jdbcTemplate.update(sqlCreateUser, params);
        user = findByEmail(user.getEmail());
        System.out.println("Got id:" + user.getUserId());
        return user;
    }

    public User findByEmail(String email) {
        Object[] params = { email };
        return jdbcTemplate.queryForObject(sqlSelectUserEmail, params, new UserRowMapper());
    }

    public User findByUserId(int userId) {
        Object[] params = { userId };
        return jdbcTemplate.queryForObject(sqlSelectUserId, params, new UserRowMapper());
    }

    public void updateUser(User oldUser, User newUser) throws SQLDataException {
        Object[] params = { newUser.getFullName(), newUser.getEmail(), newUser.getPassword(),
                            newUser.getCreatedAt(), newUser.getEnabled(), newUser.getPhotoPath(),

                            oldUser.getUserId(), oldUser.getFullName(), oldUser.getEmail(), oldUser.getPassword(),
                            oldUser.getCreatedAt(), oldUser.getEnabled(), oldUser.getPhotoPath() };
        int changedRowsCount = jdbcTemplate.update(sqlUpdateUser, params);
        if (changedRowsCount != 1)
            throw new SQLDataException();
    }

    public void deleteUser(User user) throws SQLDataException {
        Object[] params = { user.getUserId(), user.getFullName(), user.getEmail(), user.getPassword(),
                            user.getCreatedAt(), user.getEnabled(), user.getPhotoPath() };
        int changedRowsCount = jdbcTemplate.update(sqlDeleteUser, params);
        if (changedRowsCount != 1)
            throw new SQLDataException();
    }


    public List<User> getUsersByFilter(User userFilter, int paginationPosition, int paginationCount) {
        Object[] params = {
                userFilter.getFullName(),   userFilter.getFullName()  == null,
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
                userNewData.getFullName()   == null, userNewData.getFullName(),
                userNewData.getEmail()      == null, userNewData.getEmail(),
                userNewData.getPassword()   == null, userNewData.getPassword(),
                userNewData.getCreatedAt()  == null, userNewData.getCreatedAt(),
                userNewData.getEnabled()    == null, userNewData.getEnabled(),
                userNewData.getPhotoPath()  == null, userNewData.getPhotoPath(),

                userFilter.getFullName(),   userFilter.getFullName()   == null,
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
                userFilter.getFullName(),   userFilter.getFullName()   == null,
                userFilter.getEmail(),      userFilter.getEmail()      == null,
                userFilter.getPassword(),   userFilter.getPassword()   == null,
                userFilter.getCreatedAt(),  userFilter.getCreatedAt()  == null,
                userFilter.getEnabled(),    userFilter.getEnabled()    == null,
                userFilter.getPhotoPath(),  userFilter.getPhotoPath()  == null,
        };
        return jdbcTemplate.update(sqlDeleteUsersByFilter, params);
    }
}