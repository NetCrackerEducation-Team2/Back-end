package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private int notificationId;
    private NotificationObject notificationObject;
    private int notifierId;
    private boolean isRead;

    public String getNotificationMessage() {
        String action = notificationObject.getNotificationMessage().getNotificationMessageText();
        String actor = notificationObject.getUser().getFullName();
        return actor + ": " + action;
    }
}
