package com.netcraker.services;

import com.netcraker.model.Role;

import java.sql.SQLDataException;

public interface RoleService {
    Role createRole(Role role);
    Role findByRoleId(int roleId);
    Role findByRoleName(String name);
//    void deleteRole(Role role) throws SQLDataException;
//    void updateRole(Role oldRole, Role newRole);
}
