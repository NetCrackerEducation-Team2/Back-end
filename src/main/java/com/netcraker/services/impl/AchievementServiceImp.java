package com.netcraker.services.impl;

import com.netcraker.model.Achievement;
import com.netcraker.repositories.AchievementRepository;
import com.netcraker.services.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AchievementServiceImp implements AchievementService {
    private final AchievementRepository achievementRepo;

    @Override
    public Optional<Achievement> getAchievementById(int achievementId) {
        if (achievementId <= 0) {
            return Optional.empty();
        }
        return achievementRepo.getById(achievementId);
    }

    @Override
    public Optional<Achievement> updateAchievement(Achievement achievement) {
        if (achievement == null) {
            return Optional.empty();
        }
        return achievementRepo.update(achievement);
    }

    @Override
    public Optional<Achievement> createAchievement(Achievement achievement) {
        final Optional<Achievement> optFromDb = achievementRepo.getByName(achievement.getName());

        if (optFromDb.isPresent()) {
            return optFromDb;
        }
        return achievementRepo.insert(achievement);
    }

    @Override
    public boolean deleteAchievement(int achievementId) {
        final Optional<Achievement> fromDb = getAchievementById(achievementId);
        if (!fromDb.isPresent()) {
            return false;
        }
        return achievementRepo.delete(achievementId);
    }

    @Override
    public Optional<Achievement> getAchievementByName(String name) {
        if (name.trim().length() == 0) {
            return Optional.empty();
        }
        return achievementRepo.getByName(name);
    }
}
