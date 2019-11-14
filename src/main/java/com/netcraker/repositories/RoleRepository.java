package com.netcraker.repositories;

import com.netcraker.exceptions.FailedToUpdateUserException;
import com.netcraker.model.Role;
import com.netcraker.model.User;
import com.netcraker.model.mapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLDataException;
import java.sql.Timestamp;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PropertySource("classpath:sqlQueries.properties")
public class RoleRepository {
    private final JdbcTemplate jdbcTemplate;

    @Value("${role.findByRoleName}")
    private String sqlSelectRoleName;

    @Value("${role.findByRoleId}")
    private String sqlSelectRoleId;

    @Value("${role.create}")
    private String sqlCreateRole;

    @Value("${role.update}")
    private String sqlUpdateRole;

    @Value("${role.delete}")
    private String sqlDeleteRole;

    public Role findByName(String name) {
        Object[] params = { name };
        return jdbcTemplate.queryForObject(sqlSelectRoleName, params, new RoleRowMapper());
    }

    public Role findById(int roleId) {
        Object[] params = { roleId };
        return jdbcTemplate.queryForObject(sqlSelectRoleId, params, new RoleRowMapper());
    }

    public Role createRole(Role role) {
        Object[] params = { role.getName(), role.getDescription() };
        jdbcTemplate.update(sqlCreateRole, params);
        role = findByName(role.getName());
        System.out.println("Got id:" + role.getRoleId());
        return role;
    }

    public void updateRole(Role oldRole, Role newRole) {
        Object[] params = { newRole.getName(), newRole.getDescription(),

                oldRole.getName(), oldRole.getDescription() };
        int changedRowsCount = jdbcTemplate.update(sqlUpdateRole, params);

        if (changedRowsCount == 0){
            throw new FailedToUpdateUserException("Role is not found!");
        }
        if (changedRowsCount > 1){
            throw new FailedToUpdateUserException("Multiple update! Only one role can be changed!");
        }

    }

    public boolean deleteRole(Role role)  {
        Object[] params = {role.getRoleId(), role.getName(), role.getDescription() };
        int changedRowsCount = jdbcTemplate.update(sqlDeleteRole, params);
        return changedRowsCount == 1;
    }



}
