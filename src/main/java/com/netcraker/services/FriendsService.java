package com.netcraker.services;

import com.netcraker.model.FriendInvitation;
import com.netcraker.model.FriendStatus;
import com.netcraker.model.Pageable;
import com.netcraker.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FriendsService {
    List<User> getFriends(int userId);

    FriendStatus getFriendInfo(int currentUserId, int targetId);

    void sendFriendRequest(int sourceUserId, int destinationUserId);

    void deleteFromFriends(int userId, int friendId);

    List<FriendInvitation> getAwaitingFriendInvitations(Pageable pageable);

    boolean acceptFriendRequest(int invitationId);

    boolean declineFriendRequest(int invitationId);
}
