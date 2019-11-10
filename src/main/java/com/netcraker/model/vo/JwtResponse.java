package com.netcraker.model.vo;

import lombok.*;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private int userId;

    public JwtResponse() {
    }

    public JwtResponse(String token) {
        this.token = token;
    }
}
