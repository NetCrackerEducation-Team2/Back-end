package com.netcraker.services.impl;

import com.netcraker.model.Activity;
import com.netcraker.model.Page;
import com.netcraker.repositories.ActivityRepository;
import com.netcraker.services.ActivityService;
import com.netcraker.services.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final PageService pageService;

    @Override
    public Page<Activity> getActivityListForUser(int userId, int size, int page) {
        int pagesCount = pageService.getPagesCount(activityRepository.getTotalFriendsActivity(userId), size);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * size;
        return new Page<>(currentPage, pagesCount, size, activityRepository.getFriendsActivity(userId, size, offset));
    }

    @Override
    public Optional<Activity> saveActivity(Activity activity) {
        return Optional.empty();
    }
}
