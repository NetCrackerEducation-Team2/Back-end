package com.netcraker.repositories;


import com.netcraker.model.Achievement;
import com.netcraker.model.UserAchievement;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface UserAchievementRepository {
    boolean insert(int userId, int achievementId);
    boolean delete(@Nullable Integer userId, int achievementId);
    List<Achievement> getByUserId(int userId, int pageSize, int offset);
    int countByUserId(int userId);
    int countByUserIdAchievementId(int userId, int achievementId);
}
