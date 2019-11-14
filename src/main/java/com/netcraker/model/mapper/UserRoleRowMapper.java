package com.netcraker.model.mapper;

import com.netcraker.model.User;
import com.netcraker.model.UserRole;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRoleRowMapper implements RowMapper<UserRole> {

    @Override
    public UserRole mapRow(ResultSet resultSet, int i) throws SQLException {
        UserRole userRole = new UserRole();
        userRole.setUserRoleId(resultSet.getInt("users_roles_id"));
        userRole.setUserId(resultSet.getInt("user_id"));
        userRole.setRoleId(resultSet.getInt("role_id"));

        return  userRole;
    }
}
