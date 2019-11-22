package com.netcraker.services;

import org.springframework.lang.Nullable;

public interface UserAchievementService {
    boolean addUserAchievement(int userId, int achievementId);
    boolean deleteUserAchievement(@Nullable Integer userId, int achievementId);
}
