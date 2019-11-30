package com.netcraker.model.mapper;

import com.netcraker.model.UserAchievement;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAchievementRowMapper implements RowMapper<UserAchievement> {
    @Override
    public UserAchievement mapRow(ResultSet resultSet, int i) throws SQLException {
        return UserAchievement.builder()
                .achievementId(resultSet.getInt("achievements_id"))
                .userId(resultSet.getInt("user_id"))
                .creationTime(resultSet.getTimestamp("creation_time").toLocalDateTime())
                .userAchievementId(resultSet.getInt("users_achviments_id"))
                .build();
    }
}
