package com.netcraker.security;

public class SecurityConstants {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_LOGIN_URL = "/auth/login";
    public static final String SECRET_KEY = "256BIT_EXAMPLE_SECRET_KEY";
    public static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14; // 14 days
}
