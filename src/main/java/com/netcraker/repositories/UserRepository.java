package com.netcraker.repositories;

import com.netcraker.model.Role;
import com.netcraker.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseOptionalRepository<User> {
    Optional<User> findByEmail(String email);
    List<User> findById(int id);
    Optional<User> activateUser(String email);
    List<User> findByEmailOrFullNameFilterByRole(String searchExpression, Role roleFiltering, int page, int pageSize);
    List<User> findByEmailOrFullNameFilterByRoleWithout(String searchExpression, Role roleWithout, int page, int pageSize);
    int getFindByEmailOrFullNameFilterByRoleCount(String searchExpression, Role roleFiltering);
    int getFindByEmailOrFullNameFilterByRoleWithoutCount(String searchExpression, Role roleFilteringWithout);
    boolean deleteByEmail(String email);
    List<Integer> getListId();
}
