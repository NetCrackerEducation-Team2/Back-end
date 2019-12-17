package com.netcraker.services.impl;

import com.netcraker.exceptions.FailedToSendFriendRequestException;
import com.netcraker.exceptions.InvalidRequest;
import com.netcraker.exceptions.OperationForbiddenException;
import com.netcraker.exceptions.RequiresAuthenticationException;
import com.netcraker.model.*;
import com.netcraker.model.constants.NotificationTypeName;
import com.netcraker.model.constants.TableName;
import com.netcraker.repositories.FriendInvitationRepository;
import com.netcraker.repositories.FriendRepository;
import com.netcraker.services.*;
import com.netcraker.services.events.DataBaseChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendsService {
    private final FriendRepository friendRepository;
    private final FriendInvitationRepository friendInvitationRepository;
    private final UserInfoService userInfoService;
    private final PageService pageService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<User> getFriends(int userId) {
        return friendRepository.getFriendsList(userId);
    }

    @Override
    public FriendStatus getFriendInfo(int targetId) {
        Integer currentUserId = userInfoService.getCurrentUser().map(User::getUserId).orElseThrow(RequiresAuthenticationException::new);
        return getFriendInfo(currentUserId, targetId);
    }

    private FriendStatus getFriendInfo(int currentUserId, int targetUserId) {
        return FriendStatus.builder()
                .targetUserId(targetUserId).isFriend(friendRepository.isFriends(currentUserId, targetUserId))
                .isAwaitFriendRequestConfirmation(friendRepository.isAwaitingFriendRequestAccept(currentUserId, targetUserId))
                .isDeclinedFriendRequest(friendRepository.isDeclinedFriendRequest(currentUserId, targetUserId))
                .build();
    }

    @Transactional
    @Override
    public void sendFriendRequest(int sourceUserId, int destinationUserId) {
        FriendStatus friendInfo = getFriendInfo(sourceUserId, destinationUserId);
        if (!friendInfo.isFriend() && !friendInfo.isAwaitFriendRequestConfirmation() && !friendInfo.isDeclinedFriendRequest()) {
            FriendInvitation invitation = FriendInvitation.builder()
                    .invitationSource(sourceUserId)
                    .invitationTarget(destinationUserId)
                    .accepted(null)
                    .creationTime(new Timestamp(System.currentTimeMillis()))
                    .build();
            validateFriendInvitation(invitation);
            Optional<FriendInvitation> result = friendInvitationRepository.insert(
                    invitation
            );

            FriendInvitation friendInvitation = result.orElseThrow(FailedToSendFriendRequestException::new);
            notificationService.sendNotification(NotificationTypeName.INVITATIONS, generateFriendInvitationNotificationMessage(sourceUserId), friendInvitation);
        } else {
            throw new InvalidRequest("Friend request has been already sent or you are already friends or user declined your previous friend request");
        }
    }

    private void validateFriendInvitation(FriendInvitation invitation) {
        // check for adding to friend myself
        if (invitation.getInvitationSource().equals(invitation.getInvitationTarget())) {
            throw new OperationForbiddenException("Sending friend invitation to myself is not allowed");
        }
        // checking for users roles
        User userSource = userService.findByUserId(invitation.getInvitationSource());
        User userTarget = userService.findByUserId(invitation.getInvitationTarget());
        // if source is casual user than target must be user too
        boolean isSourceUser = userSource.getRoles().stream().map(Role::getName).anyMatch(roleName -> roleName.equalsIgnoreCase("USER"));
        boolean isTargetUser = userTarget.getRoles().stream().map(Role::getName).anyMatch(roleName -> roleName.equalsIgnoreCase("USER"));
        if (isSourceUser != isTargetUser) {
            throw new OperationForbiddenException("Users & admins can not be friends)");
        }
    }

    private String generateFriendInvitationNotificationMessage(int sourceUserId) {
        User sourceUser = userService.findByUserId(sourceUserId);
        return "User '" + sourceUser.getFullName() + "' (email '" + sourceUser.getEmail() + ")' sent you friend invitation";
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
        if (friendInvitation.getAccepted() != null) {
            throw new OperationForbiddenException("Message: Invitation has been already " + (friendInvitation.getAccepted() ? "accepted" : "declined"));
        }
        if (friendInvitation.getInvitationTarget().equals(user.getUserId())) {
            final int invitationSource = friendInvitation.getInvitationSource();
            final int invitationTarget = friendInvitation.getInvitationTarget();
            friendRepository.addFriends(invitationSource, invitationTarget);
            friendInvitation.setAccepted(true);
            friendInvitationRepository.update(friendInvitation);
            eventPublisher.publishEvent(new DataBaseChangeEvent<>(TableName.FRIENDS, invitationSource));
            eventPublisher.publishEvent(new DataBaseChangeEvent<>(TableName.FRIENDS, invitationTarget));
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
        if (friendInvitation.getAccepted() != null) {
            throw new OperationForbiddenException("Message: Invitation has been already " + (friendInvitation.getAccepted() ? "accepted" : "declined"));
        }
        if (friendInvitation.getInvitationTarget().equals(user.getUserId())) {
            friendInvitation.setAccepted(false);
            // TODO asem make notification here
            friendInvitationRepository.update(friendInvitation);
            return true;
        } else { // if user attempts to decline not his/her invitation
            throw new OperationForbiddenException();
        }
    }

    @Override
    public Page<User> getFriends(Pageable pageable) {
        User user = userInfoService.getCurrentUser().orElseThrow(RequiresAuthenticationException::new);
        int pagesCount = pageService.getPagesCount(friendRepository.getFriendsPageableCount(user.getUserId()), pageable.getPageSize());
        int page = pageService.getRestrictedPage(pageable.getPage(), pagesCount);
        int offset = page * pageable.getPageSize();
        return new Page<>(page, pagesCount, friendRepository.getFriendsPageable(user.getUserId(), pageable.getPageSize(), offset));
    }

    @Override
    public Optional<FriendInvitation> findFriendInvitation(int friendInvitationId) {
        Optional<FriendInvitation> invitation = friendInvitationRepository.getById(friendInvitationId);
        // check for permissions - if there is no permissions - return an empty optional
        User currentUser = userInfoService.getCurrentUser().orElseThrow(RequiresAuthenticationException::new);
        return invitation
                .filter(i ->
                        (i.getInvitationSource().equals(currentUser.getUserId()))
                                ||
                                (i.getInvitationTarget().equals(currentUser.getUserId()))
                        );
    }

    @Override
    public String getFriendRequestStatus(int friendRequestId) {
        FriendInvitation invitation = friendInvitationRepository.getById(friendRequestId).orElseThrow(NoSuchElementException::new);
        if (Objects.equals(invitation.getInvitationTarget(), userInfoService.getCurrentUser().map(User::getUserId).orElseThrow(RequiresAuthenticationException::new))) {
            Boolean status = invitation.getAccepted();
            if (status == null) {
                return "Awaiting";
            }
            return status ? "Accepted" : "Declined";
        } else {
            throw new OperationForbiddenException();
        }
    }
}
