package com.netcraker.services;

import com.netcraker.model.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface FriendsService {
    List<User> getFriends(int userId);

    Page<User> getFriends(Pageable pageable);

    FriendStatus getFriendInfo(int targetId);

    void sendFriendRequest(int sourceUserId, int destinationUserId);

    void deleteFromFriends(int userId, int friendId);

    List<FriendInvitation> getAwaitingFriendInvitations(Pageable pageable);

    boolean acceptFriendRequest(int invitationId);

    boolean declineFriendRequest(int invitationId);

    String getFriendRequestStatus(int friendRequestId);

    Optional<FriendInvitation> findFriendInvitation(int friendInvitationId);
}
