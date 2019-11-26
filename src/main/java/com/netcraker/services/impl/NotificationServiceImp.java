package com.netcraker.services.impl;

import com.netcraker.model.Notification;
import com.netcraker.repositories.NotificationRepository;
import com.netcraker.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;
    @Override
    public Optional<List<Notification>> getUserNotification(int id) {
        return notificationRepository.getUserNotifications(id, 5, 0);
    }
}
