package com.netcraker.model.vo;

import lombok.*;

@Getter
@Setter
public class JwtResponse {
    private String token;

    public JwtResponse() {
    }

    public JwtResponse(String token) {
        this.token = token;
    }
}
