package com.netcraker.services;

import com.netcraker.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Optional<List<Notification>> getUserNotification(int id);
}
