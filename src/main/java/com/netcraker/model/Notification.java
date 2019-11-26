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
        String action = notificationObject.getAction().getDescription();
        String entity = notificationObject.getEntityType().getName();
        String actor = notificationObject.getUser().getFullName();
        return actor + " " + action + " " + entity;
    }
}
