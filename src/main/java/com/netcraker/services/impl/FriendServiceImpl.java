package com.netcraker.services.impl;

import com.netcraker.exceptions.FailedToSendFriendRequestException;
import com.netcraker.exceptions.InvalidRequest;
import com.netcraker.exceptions.OperationForbiddenException;
import com.netcraker.exceptions.RequiresAuthenticationException;
import com.netcraker.model.FriendInvitation;
import com.netcraker.model.FriendStatus;
import com.netcraker.model.Pageable;
import com.netcraker.model.User;
import com.netcraker.repositories.FriendInvitationRepository;
import com.netcraker.repositories.FriendRepository;
import com.netcraker.services.FriendsService;
import com.netcraker.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendsService {
    private final FriendRepository friendRepository;
    private final FriendInvitationRepository friendInvitationRepository;
    private final UserInfoService userInfoService;

    @Override
    public List<User> getFriends(int userId) {
        return friendRepository.getFriendsList(userId);
    }

    @Override
    public FriendStatus getFriendInfo(int currentUserId, int targetId) {
        return FriendStatus.builder()
                .targetUserId(targetId).isFriend(friendRepository.isFriends(currentUserId, targetId))
                .isAwaitFriendRequestConfirmation(friendRepository.isAwaitingFriendRequestAccept(currentUserId, targetId))
                .build();
    }

    @Override
    public void sendFriendRequest(int sourceUserId, int destinationUserId) {
        FriendStatus friendInfo = getFriendInfo(sourceUserId, destinationUserId);
        if (!friendInfo.isFriend() && !friendInfo.isAwaitFriendRequestConfirmation()) {
            Optional<FriendInvitation> result = friendInvitationRepository.insert(
                    FriendInvitation.builder()
                            .invitationSource(sourceUserId)
                            .invitationTarget(destinationUserId)
                            .accepted(false)
                            .creationTime(new Timestamp(System.currentTimeMillis()))
                            .build()
            );
            result.orElseThrow(FailedToSendFriendRequestException::new);
        } else {
            throw new InvalidRequest("Friend request has been already sent or you are already friends");
        }
    }

    @Override
    public void deleteFromFriends(int userId, int friendId) {
        if (!friendRepository.deleteFromFriends(userId, friendId)) {
            throw new NoSuchElementException("Specified users are not friends");
        }
    }

    @Override
    public List<FriendInvitation> getAwaitingFriendInvitations(Pageable pageable) {
        User user = userInfoService.getCurrentUser().orElseThrow(RequiresAuthenticationException::new);
        return friendInvitationRepository.getAwaitingFriendInvitations(user.getUserId(), pageable);
    }

    @Transactional
    @Override
    public boolean acceptFriendRequest(int invitationId) {
        User user = userInfoService.getCurrentUser().orElseThrow(RequiresAuthenticationException::new);
        FriendInvitation friendInvitation = friendInvitationRepository.getById(invitationId).orElseThrow(NoSuchElementException::new);
        if (friendInvitation.getInvitationTarget().equals(user.getUserId())) {
            friendRepository.addFriends(friendInvitation.getInvitationSource(), friendInvitation.getInvitationTarget());
            friendInvitation.setAccepted(true);
            friendInvitationRepository.update(friendInvitation);
            return true;
        } else { // if user attempts to accept not his/her invitation
            throw new OperationForbiddenException();
        }
    }

    @Transactional
    @Override
    public boolean declineFriendRequest(int invitationId) {
        User user = userInfoService.getCurrentUser().orElseThrow(RequiresAuthenticationException::new);
        FriendInvitation friendInvitation = friendInvitationRepository.getById(invitationId).orElseThrow(NoSuchElementException::new);
        if (friendInvitation.getInvitationTarget().equals(user.getUserId())) {
            friendInvitation.setAccepted(false);
            friendInvitationRepository.update(friendInvitation);
            return true;
        } else { // if user attempts to decline not his/her invitation
            throw new OperationForbiddenException();
        }
    }
}
