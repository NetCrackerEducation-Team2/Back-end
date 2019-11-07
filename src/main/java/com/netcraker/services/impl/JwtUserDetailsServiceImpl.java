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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(userRepository.findByUsername(username)!=null){
            return new User(userRepository.findByUsername(username).getEmail(),
                    userRepository.findByUsername(username).getPassword(), Collections.emptyList());
        }
         throw new UsernameNotFoundException(username);
    }
}
