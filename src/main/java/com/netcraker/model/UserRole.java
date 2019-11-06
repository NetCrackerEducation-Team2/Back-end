package com.netcraker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserRole {
    private Integer user_id;
    private Integer role_id;
    private Set<String> roles;
    private String password;
    private String firstName;

    public UserRole() {
    }
}
