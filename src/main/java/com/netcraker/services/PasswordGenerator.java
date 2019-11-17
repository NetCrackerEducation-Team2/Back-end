package com.netcraker.services;

import java.security.NoSuchAlgorithmException;

@FunctionalInterface
public interface PasswordGenerator {
    int PASSWORD_LENGTH = 8;
    String generatePassword() throws NoSuchAlgorithmException;
}
