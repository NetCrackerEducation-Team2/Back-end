package com.netcraker.services;

import com.netcraker.model.Role;

import java.sql.SQLDataException;

public interface RoleService {
    Role createRole(Role role);
    Role findByRoleId(int roleId);
    Role findByRoleName(String name);
    void delete(int id);
    void update(Role role);
}
