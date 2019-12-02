package com.netcraker.controllers;

import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.FindException;
import com.netcraker.model.Achievement;
import com.netcraker.model.Page;
import com.netcraker.model.vo.AchievementReq;
import com.netcraker.services.AchievementService;
import com.netcraker.services.UserAchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
        System.out.println(achievementReq);

        if (result.hasErrors()) {
            throw newCreationException();
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(achievementService.createAchievement(achievementReq)
                        .orElseThrow(this::newCreationException));
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

    @GetMapping("user/{userId}")
    public ResponseEntity<Page<Achievement>> getUserAchievements(
            @PathVariable int userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize) {

        return ResponseEntity.ok(userAchievementService.getPage(userId, pageSize, page));
    }

    @GetMapping("user/{userId}/count")
    public ResponseEntity<Integer> countByUserId(@PathVariable int userId) {
        return ResponseEntity.ok(userAchievementService.countByUserId(userId));
    }

    private CreationException newCreationException() {
        return new CreationException("Cannot create achievement");
    }
}



