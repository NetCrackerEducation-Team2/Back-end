package com.netcraker.repositories;

import com.netcraker.model.FriendInvitation;
import com.netcraker.model.Pageable;

import java.util.List;

public interface FriendInvitationRepository extends BaseOptionalRepository<FriendInvitation> {
    List<FriendInvitation> getAwaitingFriendInvitations(int userId, Pageable pageable);
}
