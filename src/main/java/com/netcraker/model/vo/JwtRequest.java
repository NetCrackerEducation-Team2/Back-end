package com.netcraker.model.vo;

import lombok.Getter;

@Getter
public class JwtRequest {
    private String username;
    private String password;


    public JwtRequest() {
    }

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
