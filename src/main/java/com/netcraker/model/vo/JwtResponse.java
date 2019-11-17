package com.netcraker.model.vo;

import com.netcraker.model.User;
import lombok.*;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private User user;

    public JwtResponse() {
    }

    public JwtResponse(String token) {
        this.token = token;
    }
}
