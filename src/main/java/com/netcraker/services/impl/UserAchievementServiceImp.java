package com.netcraker.services.impl;

import com.netcraker.model.Achievement;
import com.netcraker.model.Page;
import com.netcraker.model.constants.TableName;
import com.netcraker.repositories.UserAchievementRepository;
import com.netcraker.services.PageService;
import com.netcraker.services.UserAchievementService;
import com.netcraker.services.events.DataBaseChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserAchievementServiceImp implements UserAchievementService {

    private final UserAchievementRepository userAchievementRepo;
    private final PageService pageService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public boolean addUserAchievement(int userId, int achievementId) {
        if (userAchievementRepo.countByUserIdAchievementId(userId, achievementId) == 0) {
            if (userAchievementRepo.insert(userId, achievementId)) {
                eventPublisher.publishEvent(new DataBaseChangeEvent<>(TableName.USERS_ACHIEVEMENTS, userId));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteUserAchievement(@Nullable Integer userId, int achievementId) {
        return userAchievementRepo.delete(userId, achievementId);
    }

    @Override
    public Page<Achievement> getPage(int userId, int pageSize, int page) {
        int pages = pageService.getPagesCount(userAchievementRepo.countByUserId(userId), pageSize);
        List<Achievement> result = userAchievementRepo.getByUserId(userId, pageSize, page * pageSize - pageSize);
        return new Page<>(page, pages, pageSize, result);
    }
}
