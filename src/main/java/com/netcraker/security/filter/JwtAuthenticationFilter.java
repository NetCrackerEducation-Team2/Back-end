package com.netcraker.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcraker.exceptions.FailedToLoginException;
import com.netcraker.model.vo.JwtRequest;
import com.netcraker.model.vo.JwtResponse;
import com.netcraker.security.SecurityConstants;
import com.netcraker.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);

        logger.info("JwtAuthenticationFilter constructed ");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        logger.info("Attempt to authenticate");

        if (!request.getMethod().equalsIgnoreCase("POST")
                && !request.getMethod().equalsIgnoreCase("OPTIONS")) {
            throw new FailedToLoginException("Only 'POST', 'OPTIONS' requests at /auth/login are allowed");
        }

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, PUT, OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
            return null;
        }
        response.addHeader("Access-Control-Allow-Origin", "*");

        final JwtRequest jwtRequest = parseJwtRequest(request);

        String email = jwtRequest.getEmail();
        String password = jwtRequest.getPassword();

        if (password != null && email != null && password.length() != 0 && email.length() != 0) {

            Authentication authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password);

            return authenticationManager.authenticate(authenticationToken);
        }
        throw new FailedToLoginException("Bad request (username and password must be not empty)");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        logger.info("Successful authentication");
        User user = ((User) authentication.getPrincipal());
        final UserService userService = getUserServiceFromContext(request);
        final com.netcraker.model.User userFromDb = userService.findByEmail(user.getUsername());
        List<String> roles = new ArrayList<>();
        userFromDb.getRoles().forEach(role -> roles.add(role.getName()));
//                user.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());

        String signingKey = SecurityConstants.SECRET_KEY;

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", userFromDb.getUserId());
        request.setAttribute("userId", userFromDb.getUserId());

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .claim("rol", roles)
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();

        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JwtResponse jwtResponse = new JwtResponse(token);
            jwtResponse.setUser(userFromDb);

            response.getWriter().write(mapper.writeValueAsString(jwtResponse));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private JwtRequest parseJwtRequest(HttpServletRequest request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(request.getInputStream(), JwtRequest.class);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return new JwtRequest();
        }
    }

    private UserService getUserServiceFromContext(HttpServletRequest req) {
        return WebApplicationContextUtils.getWebApplicationContext(req.getServletContext()).getBean(UserService.class);
    }
}
