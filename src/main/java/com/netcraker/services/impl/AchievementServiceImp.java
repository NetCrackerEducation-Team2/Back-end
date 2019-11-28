package com.netcraker.services.impl;

import com.netcraker.model.Achievement;
import com.netcraker.model.constants.TableName;
import com.netcraker.model.vo.AchievementReq;
import com.netcraker.repositories.AchievementRepository;
import com.netcraker.services.AchievementService;
import com.netcraker.services.UserAchievementService;
import com.netcraker.services.builders.imp.AchievementBuilder;
import com.netcraker.services.events.DataBaseChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AchievementServiceImp implements AchievementService {
    private final AchievementRepository achievementRepo;
    private final UserAchievementService userAchievementService;
    private final AchievementBuilder achievementBuilder;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Optional<Achievement> getAchievementById(int achievementId) {
        if (achievementId <= 0) {
            return Optional.empty();
        }
        return achievementRepo.getById(achievementId);
    }

    @NonNull
    @Override
    public List<Achievement> getAchievementsByTableName(TableName tableName) {
        List<Achievement> fromDb = achievementRepo.getByTableName(tableName);
        return fromDb != null ? fromDb : Collections.emptyList();
    }

    @Override
    public Optional<Achievement> updateAchievement(Achievement achievement) {
        if (achievement == null) {
            return Optional.empty();
        }
        return achievementRepo.update(achievement);
    }

    @Override
    public Optional<Achievement> createAchievement(AchievementReq achievementReq) {
        final Optional<Achievement> optFromDb = achievementRepo.getByName(achievementReq.getName());

        if (optFromDb.isPresent()) {
            return optFromDb;
        }

        final Achievement achievement = achievementBuilder.build(achievementReq);

        final Optional<Achievement> inserted = achievementRepo.insert(achievement);
        eventPublisher.publishEvent(new DataBaseChangeEvent<>(achievementReq.getSubject(), -1));
        return inserted;
    }

    @Transactional
    @Override
    public boolean deleteAchievement(int achievementId) {
        final Optional<Achievement> fromDb = getAchievementById(achievementId);
        if (!fromDb.isPresent()) {
            return false;
        }

        // cascade delete achievement from user_achievement table
        userAchievementService.deleteUserAchievement(null, achievementId);

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
