package com.netcraker.model.mapper;

import com.netcraker.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRowMapper implements RowMapper<Role>{
    @Override
    public Role mapRow(ResultSet resultSet, int i) throws SQLException {
        Role role = new Role();
        role.setRoleId(resultSet.getInt("users_roles_id"));
        role.setName(resultSet.getString("name"));
        role.setDescription(resultSet.getString("description"));
        return  role;
    }
}
