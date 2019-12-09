package com.netcraker.services;

import com.netcraker.model.Settings;

import java.util.Optional;

public interface SettingsService {
    Optional<Settings> findByUser(int userId);
    Settings save(Settings userSettings);
    Settings getUserSettings();
    Settings update(Settings userSettings);
}
