package com.netcraker.services.impl;

import com.netcraker.model.Notification;
import com.netcraker.model.Page;
import com.netcraker.model.vo.NotificationMessage;
import com.netcraker.repositories.NotificationRepository;
import com.netcraker.services.NotificationService;
import com.netcraker.services.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final PageService pageService;
    @Override
    public Page<NotificationMessage> getUserNotification(int id,int page, int pageSize) {
        int total = notificationRepository.getCount(id);
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        List<NotificationMessage> list = notificationRepository.getUserNotifications(id, pageSize, offset)
        .map(notifications -> notifications
                              .stream()
                              .map(notification -> NotificationMessage.builder()
                                      .notificationId(notification.getNotificationId())
                                      .notificationMessage(notification.getNotificationMessage())
                                      .creationTime(notification.getNotificationObject().getCreationTime())
                                      .build())
                              .collect(Collectors.toList()))
               .orElse(Collections.emptyList());
        return new Page<>(currentPage, pagesCount, list);
    }
}
