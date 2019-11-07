package com.netcraker.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class AuthorizationLinks {
    private int linkId;
    private Timestamp expiration;
    private String token;
    private int userId;
    private boolean isRegistrationToken;
    private boolean used;
}
