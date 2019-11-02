package com.netcraker.model.mapper;

import com.netcraker.model.User;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("ID"));
        user.setFirstName(resultSet.getString("FIRSTNAME"));
        user.setFirstName(resultSet.getString("LASTNAME"));
        user.setFirstName(resultSet.getString("EMAIL"));
        //user.getRole(rs.getString("ROLE"));
        return  user;
    }
}
