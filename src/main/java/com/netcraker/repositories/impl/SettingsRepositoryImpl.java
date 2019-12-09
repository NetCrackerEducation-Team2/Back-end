package com.netcraker.repositories.impl;

import com.netcraker.model.Settings;
import com.netcraker.model.mapper.SettingsRowMapper;
import com.netcraker.repositories.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class SettingsRepositoryImpl implements SettingsRepository {
    private final JdbcTemplate jdbcTemplate;
    @Value("${settings.getById}")
    private String sqlGetById;
    @Value("${settings.getByUserId}")
    private String sqlGetByUserId;
    @Value("${settings.insert}")
    private String sqlInsert;
    @Value("${settings.update}")
    private String sqlUpdate;
    @Value("${settings.delete}")
    private String sqlDelete;

    @Override
    public Optional<Settings> findByUserId(int userId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlGetByUserId, new SettingsRowMapper(), userId));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Settings> getById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlGetById, new SettingsRowMapper(), id));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Settings> insert(Settings settings) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, settings.getUserId());
                ps.setBoolean(2, settings.getDisableNotifications());
                ps.setBoolean(3, settings.getMakePrivate());
                ps.setBoolean(4, settings.getShowMyFavouriteBooks());
                ps.setBoolean(5, settings.getShowMyAnnouncements());
                ps.setBoolean(6, settings.getShowMyBookReviews());
                ps.setBoolean(7, settings.getShowMyBookOverviews());
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
        return getById((Integer) keyHolder.getKeys().get("setting_id"));
    }

    @Override
    public Optional<Settings> update(Settings settings) {
        try {
            jdbcTemplate.execute(sqlUpdate, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setInt(1, settings.getUserId());
                ps.setBoolean(2, settings.getDisableNotifications());
                ps.setBoolean(3, settings.getMakePrivate());
                ps.setBoolean(4, settings.getShowMyFavouriteBooks());
                ps.setBoolean(5, settings.getShowMyAnnouncements());
                ps.setBoolean(6, settings.getShowMyBookReviews());
                ps.setBoolean(7, settings.getShowMyBookOverviews());
                ps.setInt(8, settings.getSettingsId());
                return ps.execute();
            });
        } catch (DataAccessException e) {
            return Optional.empty();
        }
        return getById(settings.getSettingsId());
    }

    @Override
    public boolean delete(int id) {
        Optional<Settings> optionalSettings = getById(id);
        if (optionalSettings.isPresent()) {
            return jdbcTemplate.update(sqlDelete, id) == 1;
        }
        return false;
    }
}
