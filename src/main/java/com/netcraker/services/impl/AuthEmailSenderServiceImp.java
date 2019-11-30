package com.netcraker.services.impl;

import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.User;
import com.netcraker.services.AuthEmailSenderService;
import com.netcraker.services.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:email-messages.properties")
public class AuthEmailSenderServiceImp implements AuthEmailSenderService {

    private final MailSender mailSender;

    @Value("${subject.activation}")
    private String subjectActivation;
    @Value("${message.activation}")
    private String messageActivation;

    @Value("${subject.recoveryLink}")
    private String subjectRecoveryLink;
    @Value("${message.recoveryLink}")
    private String messageRecoveryLink;

    @Value("${subject.recoveryPassword}")
    private String subjectRecoveryPassword;
    @Value("${message.recoveryPassword}")
    private String messageRecoveryPassword;

    @Override
    public void sendActivationCode(User user, AuthorizationLinks links) {
        mailSender.send(user.getEmail(), subjectActivation, messageActivation, user.getFullName(), links.getToken());
    }

    @Override
    public void sendRecoveryLink(User user, AuthorizationLinks links) {
        mailSender.send(user.getEmail(), subjectRecoveryLink, messageRecoveryLink, user.getFullName(), links.getToken());
    }

    @Override
    public void sendNewGeneratedPassword(User user, String password) {
        mailSender.send(user.getEmail(), subjectRecoveryPassword, messageRecoveryPassword, password);
    }
}
