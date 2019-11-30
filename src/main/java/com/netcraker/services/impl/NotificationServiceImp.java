package com.netcraker.services.impl;

import com.netcraker.model.*;
import com.netcraker.model.vo.NotificationMessage;
import com.netcraker.repositories.NotificationObjectRepository;
import com.netcraker.repositories.NotificationRepository;
import com.netcraker.repositories.impl.NotificationRepositoryImp;
import com.netcraker.services.NotificationService;
import com.netcraker.services.PageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationObjectRepository notificationObjectRepository;
    private final PageService pageService;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
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

    @Override
    public <T> boolean sendNotificationToAll(int notificationTypeId, int notificationMessageId,int entityId, int userId, boolean sendAll) {
        NotificationObject nObj = NotificationServiceImp.makeNotificationObject(notificationTypeId, notificationMessageId, entityId, userId);
        nObj.setSendAll(true);
        logger.info("Trying to creating notification object: " + nObj.toString());
        notificationObjectRepository.insert(nObj);
        return true;
    }

    @Override
    @Transactional
    public <T> boolean sendNotificationToUser(int notificationTypeId, int notificationMessageId, int entityId, int userId, int notifierId) {
        NotificationObject nObj = NotificationServiceImp.makeNotificationObject(notificationTypeId, notificationMessageId, entityId, userId);
        logger.info("Trying to creating notification object: " + nObj.toString());
        NotificationObject nObjCreated = notificationObjectRepository.insert(nObj).orElse(null);
        Notification notification = NotificationServiceImp.makeNotification(nObjCreated.getNotificationObjectId(), notifierId);
        notificationRepository.insert(notification);
        return true;
    }

    private static NotificationObject makeNotificationObject(int notificationTypeId, int notificationMessageId,int entityId, int userId) {
        return NotificationObject.builder()
                .notificationType(NotificationType.builder()
                        .notificationTypeId(notificationTypeId)
                        .build())
                .notificationMessage(com.netcraker.model.NotificationMessage.builder()
                        .notificationMessageId(notificationMessageId)
                        .build())
                .entityId(entityId)
                .user(User.builder().userId(userId).build())
                .build();
    }

    private static Notification makeNotification(int notificationObjectId, int notifierId) {
        return Notification.builder()
                .notificationObject(NotificationObject.builder()
                        .notificationObjectId(notificationObjectId)
                        .build())
                .notifierId(notifierId)
                .build();
    }


}
