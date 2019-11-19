package com.netcraker.services;


public interface MailSender {
    void send(String email, String subject, String messageCascade, String... params);
}
