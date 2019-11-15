package com.netcraker.services;

import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.User;
import com.netcraker.security.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MailSender {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String username;

    public void send(User user, String subject, AuthorizationLinks authorizationLinks){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String message = createMessage(user.getFullName(),authorizationLinks.getToken());
        mailMessage.setFrom(username);
        mailMessage.setTo(user.getEmail());
        mailMessage.setText(message);
        mailMessage.setSubject(subject);

        mailSender.send(mailMessage);
    }
    private String createMessage(String fullName, String token){
        // for deploy
        // https://netcracker2-back-end.herokuapp.com
        return String.format(
                "Hello, %s! \n" +
                        "Welcome to library. " +
                        "Please visit next link: https://netcracker2-back-end.herokuapp.com%s/%s",
                fullName, SecurityConstants.AUTH_ACTIVATION_URL, token
        );
    }
}
