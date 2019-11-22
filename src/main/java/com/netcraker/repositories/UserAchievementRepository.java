package com.netcraker.repositories;


import org.springframework.lang.Nullable;

public interface UserAchievementRepository {
    boolean insert(int userId, int achievementId);
    boolean delete(@Nullable Integer userId, int achievementId);
}
