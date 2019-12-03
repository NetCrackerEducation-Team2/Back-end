package com.netcraker.services;

import com.netcraker.model.Achievement;
import com.netcraker.model.Page;
import com.netcraker.model.UserAchievement;
import org.springframework.lang.Nullable;

public interface UserAchievementService {
    boolean addUserAchievement(int userId, int achievementId);
    boolean deleteUserAchievement(@Nullable Integer userId, int achievementId);
    Page<Achievement> getPage(int userId, int pageSize, int page);
    boolean exists(int userId, int achievementId);
    int countByUserId(int userId);
}
