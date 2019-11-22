package com.netcraker.repositories.impl;


import com.netcraker.repositories.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;


@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class UserAchievementRepositoryImp implements UserAchievementRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${users_achievements.insert}")
    private String sqlInsert;
    @Value("${users_achievements.delete}")
    private String sqlDelete;

    @Override
    public boolean insert(int userId, int achievementId) {
        return jdbcTemplate.update(sqlInsert, userId, achievementId)==1;
    }

    @Override
    public boolean delete(@Nullable Integer userId, int achievementId) {
        return jdbcTemplate.update(sqlDelete, userId, achievementId) == 1;
    }
}
