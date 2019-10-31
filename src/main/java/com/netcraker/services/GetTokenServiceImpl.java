package com.netcraker.services;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetTokenServiceImpl implements GetTokenService{
    @Autowired
    private UserDetailsService userDetailsService;

    public String getToken(String username, String password)  {
        String token = "";
        if (username == null || password == null)
            return null;

        User user = (User) userDetailsService.loadUserByUsername(username);
        Map<String, Object> tokenData = new HashMap<String, Object>();
        if (password.equals(user.getPassword())) {
            tokenData.put("clientType", "user");
            tokenData.put("username", user.getUsername());
            tokenData.put("token_create_date", new Date().getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 100);
            tokenData.put("token_expiration_date", calendar.getTime());
            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setExpiration(calendar.getTime());
            jwtBuilder.setClaims(tokenData);
            String key = "abc123";
            token = jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact();
            return token;
        }
        return token;
    }

}
