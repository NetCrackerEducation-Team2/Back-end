package com.netcraker.services.impl;

import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.User;
import com.netcraker.repositories.AuthorizationRepository;
import com.netcraker.repositories.UserRepository;
import com.netcraker.services.MailSender;
import com.netcraker.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final @NonNull UserRepository userRepository;
    private final @NonNull AuthorizationRepository authorizationRepository;
    private final MailSender mailSender;

    @Override
    public ResponseEntity createUser(User user) {
        try {
            User userFromDB = userRepository.findByUsername(user.getEmail());
            if (userFromDB != null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ignored){ }
        userRepository.createUser(user);
        AuthorizationLinks authorizationLinks = new AuthorizationLinks();
        authorizationLinks.setToken(UUID.randomUUID().toString());
        authorizationLinks.setUserId(userRepository.findByUsername(user.getEmail()).getUserId());
        authorizationLinks.setRegistrationToken(true);
        authorizationLinks.setUsed(true);
        authorizationRepository.creteAuthorizationLinks(authorizationLinks);

        String message = String.format(
                "Hello, %s! \n"+
                        "Welcome to library. Please visit next link: http://localhost:8081/activate/%s",
                user.getFull_name(),
                authorizationLinks.getToken()
        );
        mailSender.send(user.getEmail(),"Activation code", message);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public boolean activateUser(String token){
        AuthorizationLinks authorizationLinks = authorizationRepository.findByActivationCode(token);
        User user = userRepository.findByUserId(authorizationLinks.getUserId());

        if(user == null){
            return false;
        }
        authorizationLinks.setUsed(false);
        authorizationRepository.updateAuthorizationLinks(authorizationLinks);
        return true;
    }

    @Override
    public User getUser(int userId) {
        return userRepository.findByUserId(userId);
    }
}