package com.netcraker.model.vo;

import com.netcraker.model.Role;
import com.netcraker.model.UserRole;

import java.sql.Time;
import java.util.Set;

public class UserVo {
    private Integer user_id;
    private String password;
    private String lastName;
    private String firstName;
    private String email;
    private Time creation_time;
    private Set<UserRole> userRoles;

    public UserVo() {
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Time getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(Time creation_time) {
        this.creation_time = creation_time;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}
