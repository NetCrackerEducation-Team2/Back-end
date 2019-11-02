package com.netcraker.security;

import com.netcraker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //User user = userService.findByUsername(username);
        // code below only for test purpose. Actually, it needs a User DAO object to get user from DB.
        if(username.equals("test")){
            return new User("test","test", Collections.emptyList());
        }
         throw new UsernameNotFoundException(username);
    }
}
