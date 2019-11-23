package com.netcraker.services;


import com.netcraker.model.User;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface RecoveryService {
    boolean sendRecoveryCode(String email);
    Optional<User> recoverPassword(String recoveryCode);
}
