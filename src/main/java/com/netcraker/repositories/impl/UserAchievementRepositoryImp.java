package com.netcraker.repositories.impl;


import com.netcraker.model.Achievement;
import com.netcraker.model.UserAchievement;
import com.netcraker.model.mapper.AchievementRowMapper;
import com.netcraker.model.mapper.UserAchievementRowMapper;
import com.netcraker.repositories.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;


@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class UserAchievementRepositoryImp implements UserAchievementRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${users_achievements.insert}")
    private String sqlInsert;
    @Value("${users_achievements.delete}")
    private String sqlDelete;
    @Value("${users_achievements.getAchievementsByUserId}")
    private String sqlGetByUserId;
    @Value("${users_achievements.countByUserId}")
    private String sqlCountByUserId;
    @Value("${users_achievements.countByUserIdAchievementId}")
    private String sqlCountByUserIdAchievementId;

    @Override
    public int countByUserIdAchievementId(int userId, int achievementId) {
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sqlCountByUserIdAchievementId, new Object[]{userId, achievementId}, Integer.class));
    }

    @Override
    public boolean insert(int userId, int achievementId) {
        return jdbcTemplate.update(sqlInsert, userId, achievementId) == 1;
    }

    @Override
    public boolean delete(@Nullable Integer userId, int achievementId) {
        return jdbcTemplate.update(sqlDelete, userId, achievementId) == 1;
    }

    @Override
    public List<Achievement> getByUserId(int userId, int pageSize, int offset) {
        return jdbcTemplate.query(sqlGetByUserId, new Object[]{userId, pageSize, offset}, new AchievementRowMapper());
    }

    @Override
    public int countByUserId(int userId) {
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sqlCountByUserId, new Object[]{userId}, Integer.class));
    }
}
