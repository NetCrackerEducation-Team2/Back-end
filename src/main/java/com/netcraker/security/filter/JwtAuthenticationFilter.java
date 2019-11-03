package com.netcraker.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcraker.model.vo.JwtRequest;
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


        String username = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();

        if (password != null && username != null) {

            System.out.println("username: " + jwtRequest.getUsername());
            System.out.println("password: " + jwtRequest.getPassword());

            Authentication authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            System.out.println("encoded password: " + passwordEncoder.encode(password));
            return authenticationManager.authenticate(authenticationToken);
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