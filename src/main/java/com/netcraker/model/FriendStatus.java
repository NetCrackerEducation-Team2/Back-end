package com.netcraker.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendStatus {
    private Integer targetUserId;
    private boolean isFriend;
    private boolean isAwaitFriendRequestConfirmation;
}
