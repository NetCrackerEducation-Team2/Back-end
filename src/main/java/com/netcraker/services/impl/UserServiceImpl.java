package com.netcraker.services.impl;

import com.netcraker.exceptions.FailedToRegisterException;
import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.User;
import com.netcraker.repositories.AuthorizationRepository;
import com.netcraker.repositories.RoleRepository;
import com.netcraker.repositories.UserRepository;
import com.netcraker.security.SecurityConstants;
import com.netcraker.services.MailSender;
import com.netcraker.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final @NonNull UserRepository userRepository;
    private final @NonNull AuthorizationRepository authorizationRepository;
    private final MailSender mailSender;


    @Override
    public User createUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getEmail());

        if (userFromDB != null) {
            throw new FailedToRegisterException("Email is already used");
        }

        final User registered = userRepository.createUser(user);

        AuthorizationLinks authorizationLinks = new AuthorizationLinks();
        authorizationLinks.setToken(UUID.randomUUID().toString());
        authorizationLinks.setUserId(registered.getUserId());
        authorizationLinks.setRegistrationToken(true);
        authorizationLinks.setUsed(true);
        authorizationRepository.creteAuthorizationLinks(authorizationLinks);

        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to library. " +
                        "Please visit next link: http://netcracker2-front-end.herokuapp.com%s/%s",
                user.getFullName(),
                SecurityConstants.AUTH_ACTIVATION_URL,
                authorizationLinks.getToken()
        );
        mailSender.send(user.getEmail(), "Activation code", message);
        return user;
    }

    public boolean activateUser(String token) {
        AuthorizationLinks authorizationLinks = authorizationRepository.findByActivationCode(token);
        User user = userRepository.findByUserId(authorizationLinks.getUserId());

        if (user == null) {
            return false;
        }
        authorizationLinks.setUsed(false);
        authorizationRepository.updateAuthorizationLinks(authorizationLinks);
        return true;
    }

}