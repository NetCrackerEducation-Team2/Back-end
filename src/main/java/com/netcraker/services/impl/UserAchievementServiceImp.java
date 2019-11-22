package com.netcraker.services.impl;

import com.netcraker.repositories.UserAchievementRepository;
import com.netcraker.services.UserAchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserAchievementServiceImp implements UserAchievementService {

    private final UserAchievementRepository userAchievementRepo;

    @Override
    public boolean addUserAchievement(int userId, int achievementId) {
        return userAchievementRepo.insert(userId, achievementId);
    }

    @Override
    public boolean deleteUserAchievement(@Nullable Integer userId, int achievementId) {
        return userAchievementRepo.delete(userId, achievementId);
    }
}
