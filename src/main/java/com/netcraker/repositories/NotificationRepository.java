package com.netcraker.repositories;

import com.netcraker.model.Notification;

import java.util.List;

public interface NotificationRepository extends BaseOptionalRepository<Notification> {
    List<Notification> getUserNotifications(int limit, int offset);
}
