package com.netcraker.repositories.impl;

import com.netcraker.model.Notification;
import com.netcraker.repositories.NotificationRepository;

import java.util.List;
import java.util.Optional;

public class NotificationRepositoryImp implements NotificationRepository {

    @Override
    public List<Notification> getUserNotifications(int limit, int offset) {
        return null;
    }

    @Override
    public Optional<Notification> getById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Notification> insert(Notification entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Notification> update(Notification entity) {
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
