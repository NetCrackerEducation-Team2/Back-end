package com.netcraker.controllers;

import com.netcraker.exceptions.RequiresAuthenticationException;
import com.netcraker.model.FriendStatus;
import com.netcraker.model.User;
import com.netcraker.services.FriendsService;
import com.netcraker.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.DELETE})
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
}
