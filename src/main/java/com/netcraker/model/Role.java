package com.netcraker.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class Role{
    private int roleId;
    @NotBlank
    private String name;
    private String description;
}
