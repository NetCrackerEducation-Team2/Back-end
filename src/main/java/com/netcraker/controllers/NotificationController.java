package com.netcraker.controllers;

import com.netcraker.exceptions.CreationException;
import com.netcraker.model.Notification;
import com.netcraker.model.Page;
import com.netcraker.model.vo.NotificationMessage;
import com.netcraker.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping({"/api/notifications"})
@CrossOrigin(methods={RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<NotificationMessage>> getNotificationMessages(
            @RequestParam int userId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int pageSize) {
        Page<NotificationMessage> pagination = notificationService.getUserNotification(userId, page, pageSize);
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }
}
