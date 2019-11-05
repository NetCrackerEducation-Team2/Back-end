package com.netcraker.model;

import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
public class User {
    private int userId;
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private Timestamp createdAt;
    private boolean enabled;
    private String photoPath;

    public User() {

    }
}