package com.netcraker.services;

import com.netcraker.model.UserRole;

public interface UserRoleService {
    UserRole findByUserId(int userId);
}
