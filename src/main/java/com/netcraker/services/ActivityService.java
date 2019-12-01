package com.netcraker.services;

import com.netcraker.model.Activity;
import com.netcraker.model.Page;

import java.util.List;
import java.util.Optional;

public interface ActivityService {
    /**
     * Returns activity list, that should be shown for user
     *
     * @param userId user's id
     * @return
     */
    Page<Activity> getActivityListForUser(int userId, int size, int page);

    Optional<Activity> saveActivity(Activity activity);
}
