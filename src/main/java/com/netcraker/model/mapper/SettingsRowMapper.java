package com.netcraker.model.mapper;

import com.netcraker.model.Settings;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SettingsRowMapper implements RowMapper<Settings> {
    @Override
    public Settings mapRow(ResultSet resultSet, int i) throws SQLException {
        return Settings.builder()
                .settingsId(resultSet.getInt("setting_id"))
                .userId(resultSet.getInt("user_id"))
                .disableNotifications(resultSet.getBoolean("disable_notification"))
                .makePrivate(resultSet.getBoolean("make_private"))
                .showMyFavouriteBooks(resultSet.getBoolean("show_my_favourite_books"))
                .showMyAnnouncements(resultSet.getBoolean("show_my_announcements"))
                .showMyBookReviews(resultSet.getBoolean("show_my_book_reviews"))
                .showMyBookOverviews(resultSet.getBoolean("show_my_book_overviews"))
                .build();
    }
}