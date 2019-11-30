package com.netcraker.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class FriendInvitation {
    private Integer invitationId;
    private Integer invitationSource;
    private Integer invitationTarget;
    private Boolean accepted;
    private Timestamp creationTime;
}
