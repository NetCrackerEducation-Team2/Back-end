package com.netcraker.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationType {
    private int notificationTypeId;
    private String notificationTypeName;
}
