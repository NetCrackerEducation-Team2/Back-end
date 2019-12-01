package com.netcraker.repositories;

import com.netcraker.model.Settings;

import java.util.Optional;

public interface SettingsRepository extends BaseOptionalRepository<Settings> {
    Optional<Settings> findByUserId(int userId);
}
