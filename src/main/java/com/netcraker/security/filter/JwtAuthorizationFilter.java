package com.netcraker.security.filter;


import com.netcraker.security.SecurityConstants;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private static  final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
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
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);

        if (!StringUtils.isEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            try {
                byte[] signingKey = SecurityConstants.SECRET_KEY.getBytes();
                Jws<Claims> parsedToken = Jwts.parser()
                        .setSigningKey(signingKey)
                        .parseClaimsJws((token.replace("Bearer", "")));

                String username = parsedToken.getBody().getSubject();

                List authorities = ((List<?>) parsedToken.getBody()
                        .get("rol")).stream()
                        .map(authority -> new SimpleGrantedAuthority((String) authority))
                        .collect(Collectors.toList());
                if (!StringUtils.isEmpty(username)) {
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
            }catch (ExpiredJwtException exception) {
                log.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
            }catch(UnsupportedJwtException exception) {
                log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
            }catch (MalformedJwtException exception) {
                log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
            }catch(SignatureException exception) {
                log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
            }catch (IllegalArgumentException exception){
                log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
            }


//                String token = header.substring(SecurityConstants.TOKEN_PREFIX.length());
//
//                String signingKey = SecurityConstants.SECRET_KEY;
//
//                Claims parsedClaims = Jwts.parser()
//                        .setSigningKey(signingKey)
//                        .parseClaimsJws(token)
//                        .getBody();
//
//                String username = parsedClaims.getSubject();

//                if (username != null && username.length() > 0) {
//                    return new UsernamePasswordAuthenticationToken(username, null, null);
//                }

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
//            catch (Exception e){
//                e.printStackTrace();
//            }
        }
        return null;
    }
}
