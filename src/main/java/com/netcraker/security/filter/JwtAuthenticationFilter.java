package com.netcraker.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcraker.model.vo.JwtRequest;
import com.netcraker.model.vo.JwtResponse;
import com.netcraker.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        System.out.println("JwtAuthenticationFilter constructed ");

        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        final JwtRequest jwtRequest = parseJwtRequest(request);

        System.out.println("Attempt to authenticate");

        String email = jwtRequest.getEmail();
        String password = jwtRequest.getPassword();

        if (password != null && email != null && password.length() != 0 && email.length() != 0) {

            System.out.println("email: " + jwtRequest.getEmail());
            System.out.println("password: " + jwtRequest.getPassword());

            Authentication authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password);


            System.out.println("encoded password: " + passwordEncoder.encode(password));
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            try {
                System.out.println("authentication is json: " +
                        new ObjectMapper().writeValueAsString(authenticationToken));
                System.out.println();
                System.out.println("authenticate is json: " +
                        new ObjectMapper().writeValueAsString(authenticate));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return authenticate;
        }
        throw new RuntimeException("Bad request (username and password must be not empty)");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        System.out.println("Successful authentication");

        User user = ((User) authentication.getPrincipal());

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String signingKey = SecurityConstants.SECRET_KEY;

        Map<String, Object> claims = new HashMap<>();

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .claim("rol", roles)
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();

        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader("Access-Control-Allow-Origin", "*");

        ObjectMapper mapper = new ObjectMapper();
        try {
            response.getWriter().write(mapper.writeValueAsString(new JwtResponse(token)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JwtRequest parseJwtRequest(HttpServletRequest request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(request.getInputStream(), JwtRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new JwtRequest();
        }
    }
}