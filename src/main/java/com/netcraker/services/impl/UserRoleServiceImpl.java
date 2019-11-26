package com.netcraker.services.impl;

import com.netcraker.model.UserRole;
import com.netcraker.repositories.impl.UserRoleRepositoryImpl;
import com.netcraker.services.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepositoryImpl userRoleRepositoryImpl;

    @Override
    public UserRole findByUserId(int userId) {
        return userRoleRepositoryImpl.getById(userId).orElse(null);
    }
}
