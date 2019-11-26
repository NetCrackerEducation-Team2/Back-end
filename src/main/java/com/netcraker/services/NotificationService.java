package com.netcraker.services;

import com.netcraker.model.Page;
import com.netcraker.model.vo.NotificationMessage;


public interface NotificationService {
    Page<NotificationMessage> getUserNotification(int id, int page, int pageSize);
}
