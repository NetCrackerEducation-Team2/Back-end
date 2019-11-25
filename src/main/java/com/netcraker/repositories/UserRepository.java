package com.netcraker.repositories;

import com.netcraker.model.Role;
import com.netcraker.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends BaseOptionalRepository<User> {
    Optional<User> findByEmail(String email);
    Optional<User> activateUser(String email);
    List<User> findByEmailOrFullNameFilterByRole(String searchExpression, Role roleFiltering);
    List<User> findByEmailOrFullNameFilterByRoleWithout(String searchExpression, Role roleWithout);
}
