package com.netcraker.repositories;

import com.netcraker.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends BaseOptionalRepository<Notification> {
    Optional<List<Notification>> getUserNotifications(int user_id, int limit, int offset);
    int getCount(int user_id);
}
