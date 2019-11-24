package com.netcraker.services.impl;

import com.netcraker.exceptions.FindException;
import com.netcraker.model.Role;
import com.netcraker.model.UserRole;
import com.netcraker.repositories.impl.UserRepositoryImpl;
import com.netcraker.repositories.impl.UserRoleRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositoryImpl userRepository;
    private final UserRoleRepositoryImpl userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Optional<com.netcraker.model.User> userOptFromDb = userRepository.findByEmail(email);

        final com.netcraker.model.User userFromDb = userOptFromDb
                .orElseThrow(() -> new UsernameNotFoundException(email));
        if (userFromDb.getEnabled()) {
            return new User(userFromDb.getEmail(), userFromDb.getPassword(), Collections.emptyList());
        }
        throw new UsernameNotFoundException(email);
    }
}
