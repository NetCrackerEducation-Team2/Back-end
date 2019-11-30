package com.netcraker.services.impl;

import com.netcraker.model.Settings;
import com.netcraker.services.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {
    @Override
    public Optional<Settings> findByUser(int userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Settings save(Settings userSettings) {
        throw new UnsupportedOperationException();
    }
}
