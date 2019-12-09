package com.netcraker.services.impl;

import com.netcraker.exceptions.OperationForbiddenException;
import com.netcraker.exceptions.RequiresAuthenticationException;
import com.netcraker.model.Settings;
import com.netcraker.model.User;
import com.netcraker.repositories.SettingsRepository;
import com.netcraker.services.SettingsService;
import com.netcraker.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {
    private final SettingsRepository settingsRepository;
    private final UserInfoService userInfoService;

    @Override
    public Optional<Settings> findByUser(int userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Settings save(Settings userSettings) {
        throw new UnsupportedOperationException();
    }

    @Transactional
    @Override
    public Settings getUserSettings() {
        User currentUser = userInfoService.getCurrentUser().orElseThrow(RequiresAuthenticationException::new);
        Optional<Settings> userSettings = settingsRepository.findByUserId(currentUser.getUserId());
        Settings settings = userSettings.orElseGet(() -> settingsRepository.insert(
                Settings.builder()
                        .userId(currentUser.getUserId())
                        .disableNotifications(false)
                        .makePrivate(false)
                        .showMyFavouriteBooks(false)
                        .showMyAnnouncements(false)
                        .showMyBookOverviews(false)
                        .showMyBookReviews(false)
                        .build()
        ).orElseThrow(InternalError::new));
        return settings;
    }

    @Override
    public Settings update(Settings userSettings) {
        User currentUser = userInfoService.getCurrentUser().orElseThrow(RequiresAuthenticationException::new);
        if (Objects.equals(userSettings.getUserId(), currentUser.getUserId())) {
            return settingsRepository.update(userSettings).orElseThrow(InternalError::new);
        } else {
            throw new OperationForbiddenException("You can update only your settings");
        }
    }
}
