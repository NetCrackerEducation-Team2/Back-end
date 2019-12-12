package com.netcraker.model;

import com.netcraker.model.annotations.EntityId;
import com.netcraker.model.annotations.GenericModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@GenericModel("notification_messages")
public class NotificationMessage implements Entity{
    @EntityId("notification_message_id")
    private int notificationMessageId;
    private String notificationMessageText;

    @Override
    public Integer getId() {
        return notificationMessageId;
    }
}
