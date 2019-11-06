package com.netcraker.services.impl;

import com.netcraker.model.User;
import com.netcraker.repositories.RoleRepository;
import com.netcraker.repositories.UserRepository;
import com.netcraker.services.UserService;
import com.netcraker.services.impl.JwtUserDetailsServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final @NonNull UserRepository userRepository;
    private final @NonNull RoleRepository roleRepository;

    @Override
    public User createUser(User user) {
        return  userRepository.createUser(user);
    }

}