package com.netcraker.security.filter;

import com.netcraker.security.SecurityConstants;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        logger.info("JwtAuthorizationFilter constructed ");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        logger.info("JwtAuthorizationFilter is doing filter");
        logger.info("Method: " + request.getMethod());
        logger.info("URI: " + request.getRequestURI());

        if (request.getRequestURI().startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = getAuthentication(request);
        if (authentication == null) {
            logger.warn("Authentication is null");
            filterChain.doFilter(request, response);
            return;
        }
        logger.info("Authentication set in Security context holder");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String header = request.getHeader(SecurityConstants.TOKEN_HEADER);

        if (header != null && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            try {
                String token = header.substring(SecurityConstants.TOKEN_PREFIX.length());

                String signingKey = SecurityConstants.SECRET_KEY;

                Claims parsedClaims = Jwts.parser()
                        .setSigningKey(signingKey)
                        .parseClaimsJws(token)
                        .getBody();

                String username = parsedClaims.getSubject();
                request.setAttribute("currentUserEmail", username);
                if (username != null && username.length() > 0) {
                    return new UsernamePasswordAuthenticationToken(username, null, null);
                }
            }
//            catch (ExpiredJwtException exception) {
////                s("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
//            } catch (UnsupportedJwtException exception) {
////                log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
//            } catch (MalformedJwtException exception) {
////                log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
//            } catch (SignatureException exception) {
////                log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
//            } catch (IllegalArgumentException exception) {
////                log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
//            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
