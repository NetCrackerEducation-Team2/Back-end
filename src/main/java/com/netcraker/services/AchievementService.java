package com.netcraker.services;

import com.netcraker.model.Achievement;

import java.util.Optional;

public interface AchievementService {
    Optional<Achievement> getAchievementById(int achievementId);
    Optional<Achievement> getAchievementByName(String name);
    Optional<Achievement> updateAchievement(Achievement achievement);
    Optional<Achievement> createAchievement(Achievement achievement);
    boolean deleteAchievement(int achievementId);
}
