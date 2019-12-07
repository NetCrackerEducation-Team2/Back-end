package com.netcraker.services;

import com.netcraker.model.User;

import java.util.Optional;

/**
 * Current authenticated user provider
 */
public interface UserInfoService {
    Optional<User> getCurrentUser();
}
