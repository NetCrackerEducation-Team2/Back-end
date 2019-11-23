package com.netcraker.services;


import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.User;

public interface AuthEmailSenderService {
    void sendActivationCode(User user, AuthorizationLinks links);
    void sendRecoveryLink(User user, AuthorizationLinks links);
    void sendNewGeneratedPassword(User user, String password);
}
