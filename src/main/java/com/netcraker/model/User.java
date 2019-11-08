package com.netcraker.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Getter
@Setter
public class User {
    private int userId;
    @NotBlank
    private String fullName;
    @Email @NotBlank
    private String email;
    @Size(min = 6)
    private String password;
    private Timestamp createdAt;
    private boolean enabled;
    private String photoPath;
}