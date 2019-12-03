package com.netcraker.services.impl;

import com.netcraker.model.*;
import com.netcraker.model.constants.NotificationTypeMessage;
import com.netcraker.model.constants.NotificationTypeName;
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
        int total = notificationRepository.getUserNotificationsCount(id);
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
    @Transactional
    public <T> boolean sendNotification(NotificationTypeName notificationTypeName, NotificationTypeMessage notificationTypeMessage, T entity) {
        int notificationTypeId = 0;
        int notificationMessageId = getNotificationMessageId(notificationTypeMessage);
        int entityId = 0;
        int userId = 0;
        int notifierId = 0;
        boolean sendAll = false;
        switch (notificationTypeName) {
            case ANNOUNCEMENTS:
                notificationTypeId = 10;
                entityId = ((Announcement) entity).getAnnouncementId();
                userId = ((Announcement) entity).getUserId();
                notifierId = userId;
                break;
            case BOOK_REVIEWS:
                notificationTypeId = 11;
                entityId = ((BookReview) entity).getBookReviewId();
                userId = ((BookReview) entity).getUserId();
                notifierId = userId;
                break;
        }

        NotificationObject nObj = NotificationServiceImp.makeNotificationObject(notificationTypeId, notificationMessageId, entityId, userId);
        nObj.setSendAll(sendAll);
        logger.info("Trying to creating notification object: " + nObj.toString());
        Optional<NotificationObject> nObjCreated = notificationObjectRepository.insert(nObj);
        if(!sendAll && nObjCreated.isPresent()) {
            NotificationObject notificationObject = nObjCreated.orElse(null);
            Notification notification = NotificationServiceImp.makeNotification(notificationObject.getNotificationObjectId(), notifierId);
            notificationRepository.insert(notification);
        }
        return true;
    }

    private int getNotificationMessageId(NotificationTypeMessage notificationTypeMessage) {
        int notificationMessageId = 0;
        switch (notificationTypeMessage) {
            case CREATE_ANNOUNCEMENT:
                notificationMessageId = 12;
                break;
            case PUBLISH_ANNOUNCEMENT:
                notificationMessageId = 15;
                break;
            case CREATE_BOOK_REVIEWS:
                notificationMessageId = 13;
                break;
            case PUBLISH_BOOK_REVIEWS:
                notificationMessageId = 14;
                break;

        }
        return notificationMessageId;
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
