package com.netcraker.security.filter;

import com.netcraker.security.SecurityConstants;
import io.jsonwebtoken.*;
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
import java.util.Enumeration;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        System.out.println("JwtAuthorizationFilter constructed ");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        System.out.println("JwtAuthorizationFilter is doing filter");

        Authentication authentication = getAuthentication(request);
        if (authentication == null) {
            System.out.println("Authentication is null");
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("Authentication set in Security context holder");
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
