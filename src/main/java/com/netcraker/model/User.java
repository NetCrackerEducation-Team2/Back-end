package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int userId;
    private String full_name;
    private String email;
    private String password;
    private Timestamp createdAt;
    private boolean enabled;
    private String photoPath;
}
