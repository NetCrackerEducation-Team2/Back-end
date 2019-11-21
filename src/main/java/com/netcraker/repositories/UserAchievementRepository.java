package com.netcraker.repositories;


public interface UserAchievementRepository {
    boolean insert(int userId, int achievementId);
    boolean delete(int userId, int achievementId);
}
