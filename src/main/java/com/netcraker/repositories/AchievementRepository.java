package com.netcraker.repositories;

import com.netcraker.model.Achievement;

import java.util.Optional;

public interface AchievementRepository extends BaseOptionalRepository<Achievement> {
    Optional<Achievement> getByName(String name);
}
