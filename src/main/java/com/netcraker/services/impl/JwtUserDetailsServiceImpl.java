package com.netcraker.services.impl;
import com.netcraker.repositories.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final @NonNull UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final com.netcraker.model.User userFromDb = userRepository.findByEmail(email);

        if(userFromDb!=null){
            return new User(userFromDb.getEmail(), userFromDb.getPassword(), Collections.emptyList());
        }
         throw new UsernameNotFoundException(email);
    }
}
