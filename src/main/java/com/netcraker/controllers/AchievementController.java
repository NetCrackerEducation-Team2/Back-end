package com.netcraker.controllers;

import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.FindException;
import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.Achievement;
import com.netcraker.services.AchievementService;
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

    @PostMapping
    public ResponseEntity<Achievement> createAchievement(@RequestBody @Validated Achievement achievement,
                                                         BindingResult result) {
        validateBody(result);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(achievementService.createAchievement(achievement)
                        .orElseThrow(() -> new CreationException("Cannot create achievement")));
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
}



