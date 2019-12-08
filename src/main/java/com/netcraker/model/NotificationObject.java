package com.netcraker.model;

import com.netcraker.model.annotations.EntityId;
import com.netcraker.model.annotations.GenericModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@GenericModel("notification_objects")
public class NotificationObject implements Entity {
    @EntityId("notification_object_id")
    private int notificationObjectId;
    private NotificationType notificationType;
    private int entityId;
    private NotificationMessage notificationMessage;
    private LocalDateTime creationTime;
    private User user;
    private boolean isRead;
    private boolean sendAll;

    @Override
    public Integer getId() {
        return getNotificationObjectId();
    }
}
