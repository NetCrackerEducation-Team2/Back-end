package com.netcraker.services;

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

    public void send(String emailTo, String subject, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setText(message);
        mailMessage.setSubject(subject);

        mailSender.send(mailMessage);
    }
}
