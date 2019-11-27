package com.netcraker.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcraker.controllers.RegistrationController;
import com.netcraker.exceptions.FailedToLoginException;
import com.netcraker.model.Role;
import com.netcraker.model.UserRole;
import com.netcraker.model.vo.JwtRequest;
import com.netcraker.model.vo.JwtResponse;
import com.netcraker.repositories.RoleRepository;
import com.netcraker.repositories.UserRoleRepository;
import com.netcraker.repositories.impl.RoleRepositoryImpl;
import com.netcraker.repositories.impl.UserRoleRepositoryImpl;
import com.netcraker.security.SecurityConstants;
import com.netcraker.services.UserRoleService;
import com.netcraker.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


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

        logger.info("Method: " + request.getMethod());

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

            logger.info("email: " + jwtRequest.getEmail());
            logger.info("password: " + jwtRequest.getPassword());

            Authentication authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password);


            logger.info("encoded password: " + passwordEncoder.encode(password));
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            try {
                logger.info("authentication in json: " +
                        new ObjectMapper().writeValueAsString(authenticationToken));
                logger.info("authenticate in json: " +
                        new ObjectMapper().writeValueAsString(authenticate));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return authenticate;
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

    private UserService getUserServiceFromContext(HttpServletRequest req) {
        return WebApplicationContextUtils.getWebApplicationContext(req.getServletContext()).getBean(UserService.class);
    }
}