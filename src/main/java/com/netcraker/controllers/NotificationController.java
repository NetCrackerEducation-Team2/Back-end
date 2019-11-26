package com.netcraker.controllers;

import com.netcraker.exceptions.CreationException;
import com.netcraker.model.Notification;
import com.netcraker.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/notifications"})
@CrossOrigin(methods={RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getAnnouncements(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int pageSize) {
        return ResponseEntity.ok().body(notificationService.getUserNotification(1)
                .orElseThrow(() -> new CreationException("Cannot find announcement by id")));
    }
}
