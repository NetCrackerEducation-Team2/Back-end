package com.netcraker.services;

import com.netcraker.model.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendsService {
    List<User> getFriends(int userId);

    Page<User> getFriends(Pageable pageable);

    FriendStatus getFriendInfo(int currentUserId, int targetId);

    void sendFriendRequest(int sourceUserId, int destinationUserId);

    void deleteFromFriends(int userId, int friendId);

    List<FriendInvitation> getAwaitingFriendInvitations(Pageable pageable);

    boolean acceptFriendRequest(int invitationId);

    boolean declineFriendRequest(int invitationId);
}
