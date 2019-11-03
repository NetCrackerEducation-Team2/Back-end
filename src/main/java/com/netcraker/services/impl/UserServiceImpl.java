package com.netcraker.services.impl;

import com.netcraker.model.User;
import com.netcraker.repositories.RoleRepository;
import com.netcraker.repositories.UserRepository;
import com.netcraker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User saveUser(User user) {
        //Role roleUser = roleRepository.findById("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        //userRoles.add(roleUser);
        //user.setPassword(password);
        //user.setRole(userRoles);
        //user.setStatus(Status.ACTIVE);
        //User registeredUser = userRepository.save(user);

        return null;//registeredUser
    }

    @Override
    public User getUser(String username, String password) {
        return userRepository.getUser(username,password);
    }

    @Override
    public void deleteUser(Long id) {
       // userRepository.deleteById(id);
    }
}