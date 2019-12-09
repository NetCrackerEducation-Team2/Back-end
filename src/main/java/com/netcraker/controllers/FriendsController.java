package com.netcraker.controllers;

import com.netcraker.exceptions.RequiresAuthenticationException;
import com.netcraker.model.*;
import com.netcraker.services.FriendsService;
import com.netcraker.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
public class FriendsController extends BaseController {
    private final FriendsService friendsService;

    public FriendsController(UserService userService, FriendsService friendsService) {
        super(userService);
        this.friendsService = friendsService;
    }

    @GetMapping("/friends/getFriendInfo")
    public FriendStatus getFriendInfo(@RequestParam int targetUserId) {
        return friendsService.getFriendInfo(getCurrentUser().map(User::getUserId).orElseThrow(RequiresAuthenticationException::new), targetUserId);
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<User>> getFriends(@PathVariable int userId) {
        List<User> friends = friendsService.getFriends(userId);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @PostMapping("/friends/friendRequest")
    public ResponseEntity<String> sendFriendRequest(@RequestParam int destinationUserId) {
        friendsService.sendFriendRequest(getCurrentUser().map(User::getUserId).orElseThrow(RequiresAuthenticationException::new), destinationUserId);
        // TODO asem should we return OK status or CREATED? If 'CREATED' how to pass blocking access to non OK response at frontend side?
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/friends/friendRequest")
    public ResponseEntity<String> deleteFromFriends(@RequestParam int friendId) {
        friendsService.deleteFromFriends(getCurrentUser().map(User::getUserId).orElseThrow(RequiresAuthenticationException::new), friendId);
        // TODO should be returned OK status? Maybe we should pass own status code in response body?
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/friends/awaitingFriendRequests")
    public List<FriendInvitation> getAllAwaitingFriendRequests(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "5") int pageSize) {
        return friendsService.getAwaitingFriendInvitations(Pageable.of(page, pageSize));
    }

    @CrossOrigin
    @PutMapping("/friend/friendRequest/accept/{invitationId}")
    public ResponseEntity<Boolean> acceptFriendRequest(@PathVariable int invitationId) {
        return new ResponseEntity<>(friendsService.acceptFriendRequest(invitationId), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/friend/friendRequest/decline/{invitationId}")
    public ResponseEntity<Boolean> declineFriendRequest(@PathVariable int invitationId) {
        return new ResponseEntity<>(friendsService.declineFriendRequest(invitationId), HttpStatus.OK);
    }

    @GetMapping("/friends")
    public Page<User> getFriends(@RequestParam int page, @RequestParam int pageSize) {
        Page<User> friends = friendsService.getFriends(Pageable.of(page, pageSize));
        return friends;
    }
}
