package com.netcraker.model.mapper;

import com.netcraker.model.User;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setFirstName(resultSet.getString("first_name"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEnabled(true);
        //user.setCreatedAt();
        user.setPhotoPath(resultSet.getString("photo_path"));
        return  user;
    }
}
