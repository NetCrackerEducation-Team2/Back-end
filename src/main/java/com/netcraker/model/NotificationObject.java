package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationObject {
    private int notificationObjectId;
    private NotificationType notificationType;
    private int entityId;
    private NotificationMessage notificationMessage;
    private LocalDateTime creationTime;
    private User user;
    private boolean isRead;
    private boolean sendAll;
}
