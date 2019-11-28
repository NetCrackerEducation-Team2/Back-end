package com.netcraker.controllers;

import com.netcraker.Application;
import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.FindException;
import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.Achievement;
import com.netcraker.model.Page;
import com.netcraker.model.UserAchievement;
import com.netcraker.model.vo.AchievementReq;
import com.netcraker.services.AchievementService;
import com.netcraker.services.UserAchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;


@RestController
@CrossOrigin(methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RequestMapping({"/api/achievement"})
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;
    private final UserAchievementService userAchievementService;

    @PostMapping
    public ResponseEntity<Achievement> createAchievement(@RequestBody @Validated AchievementReq achievementReq,
                                                         BindingResult result) {
        if (result.hasErrors()) {
            throw newCreationException();
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(achievementService.createAchievement(achievementReq)
                        .orElseThrow(this::newCreationException));
    }

    @PutMapping
    public ResponseEntity<Achievement> updateAchievement(@RequestBody @Validated Achievement achievement,
                                                         BindingResult result) {
        validateBody(result);

        return ResponseEntity.ok(achievementService.updateAchievement(achievement)
                .orElseThrow(() -> new UpdateException("Cannot update achievement")));
    }

    @DeleteMapping("/{achievementId}")
    public boolean deleteAchievement(@PathVariable int achievementId) {
        return achievementService.deleteAchievement(achievementId);
    }

    @GetMapping("/{achievementId}")
    public ResponseEntity<Achievement> getAchievement(@PathVariable int achievementId) {
        return ResponseEntity.ok(achievementService.getAchievementById(achievementId)
                .orElseThrow(() -> new FindException("Cannot find achievement")));
    }

    private void validateBody(BindingResult result) {
        if (result.hasErrors()) {
            throw new CreationException("Achievement values must be valid");
        }
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<Page<Achievement>> getUserAchievements(
            @PathVariable int userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize) {

        return ResponseEntity.ok(userAchievementService.getPage(userId, pageSize, page));
    }

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/messages")
    public String greeting(String message) throws Exception {
//        System.out.println("greeting controller, message : " + message);
//        System.out.println("greeting controller will sleep for 1.5s...");
//        Thread.sleep(1500); // simulated delay

//        simpMessagingTemplate
//                .convertAndSend("/topic/messages", "message from server");

        return "Hello, " + HtmlUtils.htmlEscape("Your message: " + message + "!");
    }

    @SubscribeMapping("/subscribe/{userId}")
    public String subscribe(@DestinationVariable int userId) throws InterruptedException {
        System.err.println("greeting controller");

        return "subscribed, your userId : " + userId;
    }

    private CreationException newCreationException() {
        return new CreationException("Cannot create achievement");
    }
}



