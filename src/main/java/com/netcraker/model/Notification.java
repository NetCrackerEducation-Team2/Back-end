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
}
