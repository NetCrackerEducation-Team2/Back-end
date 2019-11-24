package com.netcraker.services;


@FunctionalInterface
public interface PasswordGenerator {
    int PASSWORD_LENGTH = 8;
    String generatePassword();
}
