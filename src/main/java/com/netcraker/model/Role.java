package com.netcraker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Role{

    private Integer role_id;
    private String name;
    private String description;
    private Set<UserRole> userRoles;

}
