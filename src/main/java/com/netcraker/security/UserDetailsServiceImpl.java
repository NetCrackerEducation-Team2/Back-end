package com.netcraker.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // code below only for test purpose. Actually, it needs a User DAO object to get user from DB.
        if(username.equals("test")){
            return new User("test","test", Collections.emptyList());
        }
         throw new UsernameNotFoundException(username);
    }
}
