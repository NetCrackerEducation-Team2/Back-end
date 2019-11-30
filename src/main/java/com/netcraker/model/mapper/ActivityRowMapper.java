package com.netcraker.model.mapper;

import com.netcraker.model.Activity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ActivityRowMapper implements RowMapper<Activity> {
    @Override
    public Activity mapRow(ResultSet resultSet, int i) throws SQLException {
        return Activity.builder()
                .activityId(resultSet.getInt("activity_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .userId(resultSet.getInt("user_id"))
                .creationTime(resultSet.getTimestamp("creation_time"))
                .build();
    }
}
