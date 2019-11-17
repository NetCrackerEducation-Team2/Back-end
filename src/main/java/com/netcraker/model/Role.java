package com.netcraker.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class Role{
    private int roleId;
    @NotBlank
    private String name;
    private String description;
    private List<User> userList;
}
