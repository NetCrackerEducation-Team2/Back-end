package com.netcraker.services.impl;

import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.RequiresAuthenticationException;
import com.netcraker.model.*;
import com.netcraker.model.constants.NotificationTypeMessage;
import com.netcraker.model.constants.NotificationTypeName;
import com.netcraker.model.vo.NotificationMessage;
import com.netcraker.repositories.NotificationMessageRepository;
import com.netcraker.repositories.NotificationObjectRepository;
import com.netcraker.repositories.NotificationRepository;
import com.netcraker.services.NotificationService;
import com.netcraker.services.PageService;
import com.netcraker.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImp implements NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepository notificationRepository;
    private final NotificationObjectRepository notificationObjectRepository;
    private final PageService pageService;
    private final UserInfoService userInfoService;
    private final NotificationMessageRepository notificationMessageRepository;
    private final static String destinationTopic = "/topic/notifications/";

    private static NotificationObject makeNotificationObject(int notificationTypeId, int notificationMessageId, int entityId, int userId) {
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

    @Override
    public Page<NotificationMessage> getUserNotificationMessages(int id, int page, int pageSize) {
        Page<Notification> userNotifications = getUserNotifications(Pageable.of(page, pageSize));
        return new Page<>(userNotifications.getCurrentPage(),
                userNotifications.getCountPages(),
                userNotifications.getArray().stream()
                        .map(notification -> NotificationMessage.builder()
                                .notificationId(notification.getNotificationId())
                                .notificationMessage(notification.getNotificationMessage())
                                .creationTime(notification.getNotificationObject().getCreationTime())
                                .build()
                        ).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public <T> boolean sendNotification(NotificationTypeName notificationTypeName, NotificationTypeMessage notificationTypeMessage, T entity) {
        int notificationMessageId = getNotificationMessageId(notificationTypeMessage);
        return sendNotification(notificationTypeName, notificationMessageId, entity);
    }

    @Transactional
    @Override
    public <T> boolean sendNotification(NotificationTypeName notificationTypeName, String notificationMessage, T entity) {
        return sendNotification(notificationTypeName, insertNewNotificationMessage(notificationMessage), entity);
    }

    private <T> boolean sendNotification(NotificationTypeName notificationTypeName, int notificationMessageId, T entity) {
        int notificationTypeId;
        int entityId;
        int userId;
        int notifierId;
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
            case INVITATIONS:
                FriendInvitation invitation = (FriendInvitation) entity;
                notificationTypeId = 13;
                entityId = invitation.getInvitationId();
                userId = invitation.getInvitationTarget();
                notifierId = invitation.getInvitationTarget(); // FIXME why notifier ID is a user, who should receive notification, not sender?
                break;
            default:
                throw new UnsupportedOperationException("Unexpected notification type " + notificationTypeName);
        }

        NotificationObject nObj = NotificationServiceImp.makeNotificationObject(notificationTypeId, notificationMessageId, entityId, userId);
        nObj.setSendAll(sendAll);
        log.info("Trying to creating notification object: " + nObj.toString());
        Optional<NotificationObject> nObjCreated = notificationObjectRepository.insert(nObj);
        if (!nObjCreated.isPresent()) {
            throw new CreationException("Error in creating notification object!");
        }
        if (!sendAll) {
            NotificationObject notificationObject = nObjCreated.orElse(null);
            Notification notification = NotificationServiceImp.makeNotification(notificationObject.getNotificationObjectId(), notifierId);

            Optional<Notification> createdNotification = notificationRepository.insert(notification);
            if (!createdNotification.isPresent()) {
                throw new CreationException("Error in creating notification!");
            }
            System.out.println(notifierId);
            this.simpMessagingTemplate.convertAndSend(destinationTopic + notifierId, makeNotificationMessage(createdNotification.orElse(null)));
        }
        return true;
    }

    private NotificationMessage makeNotificationMessage(Notification notification){
        return NotificationMessage.builder()
                .notificationId(notification.getNotificationId())
                .notificationMessage(notification.getNotificationMessage())
                .creationTime(notification.getNotificationObject().getCreationTime())
                .build();
    }

    private int insertNewNotificationMessage(String message) {
        return notificationMessageRepository.insert(
                com.netcraker.model.NotificationMessage
                        .builder()
                        .notificationMessageText(message)
                        .build()
        ).orElseThrow(() -> new InternalError("Could not insert notification message")).getId();
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
            case RECEIVED_FRIEND_INVITATION:
                notificationMessageId = 11;
                break;
        }
        return notificationMessageId;
    }

    @Override
    public Page<Notification> getUserNotifications(Pageable pageable) {
        User user = userInfoService.getCurrentUser().orElseThrow(RequiresAuthenticationException::new);
        int total = notificationRepository.getUserNotificationsCount(user.getUserId());
        int pagesCount = pageService.getPagesCount(total, pageable.getPageSize());
        int currentPage = pageService.getRestrictedPage(pageable.getPage(), pagesCount);
        int offset = currentPage * pageable.getPageSize();
        List<Notification> list = notificationRepository.getUserNotifications(user.getUserId(), pageable.getPageSize(), offset).orElse(Collections.emptyList());
        return new Page<>(currentPage, pagesCount, list);
    }
}
