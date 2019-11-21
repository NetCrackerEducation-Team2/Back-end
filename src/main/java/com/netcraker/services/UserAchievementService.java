package com.netcraker.services;

import com.netcraker.model.UserAchievement;

public interface UserAchievementService {
    boolean addUserAchievement(int userId, int achievementId);
    boolean deleteUserAchievement(int userId, int achievementId);
}
