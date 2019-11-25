package com.netcraker.controllers;

import com.netcraker.model.Activity;
import com.netcraker.model.Page;
import com.netcraker.model.User;
import com.netcraker.services.ActivityService;
import com.netcraker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping({"/api"})
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;
    private final UserService userService;

    @GetMapping("/activity/lastFriendActivities")
    public Page<Activity> getFriendsActivities(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            HttpServletRequest request
    ) {
        String currentUserEmail = (String) request.getAttribute("currentUserEmail");
        User currentUser = userService.findByEmail(currentUserEmail);
        return activityService.getActivityListForUser(currentUser.getUserId(), pageSize, page);
    }
}
