package com.netcraker.repositories;

import com.netcraker.model.Role;
import com.netcraker.model.User;
import com.netcraker.model.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends BaseOptionalRepository<UserRole> {
//    Optional<UserRole> update(User user, Role role, int oldRole);
    Optional<UserRole> insert(User user, Role role);
    List<UserRole> getAll(int userId);
}
