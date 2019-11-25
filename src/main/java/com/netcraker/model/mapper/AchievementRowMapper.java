package com.netcraker.model.mapper;

import com.netcraker.model.Achievement;
import com.netcraker.model.Announcement;
import com.netcraker.model.constants.TableName;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AchievementRowMapper implements RowMapper<Achievement> {
    @Override
    public Achievement mapRow(ResultSet resultSet, int i) throws SQLException {
        return Achievement.builder()
                .achievementId(resultSet.getInt("achievement_id"))
                .name(resultSet.getString("name"))
                .requirement(resultSet.getString("requirment"))
                .tableName(TableName.valueOf(resultSet.getString("table_name").toUpperCase()))
                .build();
    }
}
