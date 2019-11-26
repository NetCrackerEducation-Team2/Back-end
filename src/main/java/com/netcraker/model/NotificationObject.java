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
    private EntityType entityType;
    private int entityId;
    private NotificationAction action;
    private LocalDateTime creationTime;
    private User user;
}
