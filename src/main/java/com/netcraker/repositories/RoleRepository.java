package com.netcraker.repositories;

import com.netcraker.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends BaseOptionalRepository<Role> {
    Optional<Role> findByName(String name);
    List<Role> getAllRoles();
    List<Role> getAllRoleById(int userId);
    Role getRoleByUserId(int userId);
}
