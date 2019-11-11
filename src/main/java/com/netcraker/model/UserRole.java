package com.netcraker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserRole {
    private int userRoleId;
    private int userId;
    private int roleId;
}
