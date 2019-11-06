package com.netcraker.repositories;
import com.netcraker.model.User;
import com.netcraker.model.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.*;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public User createUser(User user) {
        jdbcTemplate.update("INSERT INTO users (first_name, last_name, password, email, creation_time, enabled, photo_path)" +
                                 "VALUES(?, ?, ?, ?, ?, ?, ?)",
                                  new Object[] { user.getFirstName(), user.getLastName(), user.getPassword(), user.getEmail(),
                                          new Timestamp(System.currentTimeMillis()), true, user.getPhotoPath()});
        return user;
    }

    public User findByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE first_name=?",
                new Object[]{ username}, new UserRowMapper());
    }
}