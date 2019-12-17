package com.netcraker.repositories.impl;

import com.netcraker.model.Activity;
import com.netcraker.model.mapper.ActivityRowMapper;
import com.netcraker.repositories.ActivityRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepository {
    private final JdbcTemplate jdbcTemplate;
    @Value("${activity.sqlFindByUserIdInList}")
    private String sqlFindByUserIdInList;
    @Value("${activity.sqlFindFriendsActivity}")
    private String sqlFindFriendsListActivity;
    @Value("${activity.getFriendsListActivitySize}")
    private String sqlGetFriendsListActivitySize;
    @Value("${activity.sqlGetById}")
    private String sqlGetById;
    @Value("${activity.sqlInsert}")
    private String sqlInsert;
    @Value("${activity.sqlUpdate}")
    private String sqlUpdate;
    @Value("${activity.sqlDelete}")
    private String sqlDelete;

    @Override
    public List<Activity> getActivityByUsersId(List<Integer> usersIds, int size, int offset) {
        return jdbcTemplate.query(sqlFindByUserIdInList,
                new ActivityRowMapper(),
                usersIds.stream().map(Object::toString).collect(Collectors.joining(",")), offset, size);
    }

    @Override
    public List<Activity> getFriendsActivity(int userId, int size, int offset) {
        return jdbcTemplate.query(sqlFindFriendsListActivity, new ActivityRowMapper(), userId, userId, size, offset);
    }

    @Override
    public int getTotalFriendsActivity(int userId) {
        return jdbcTemplate.queryForObject(sqlGetFriendsListActivitySize, int.class, userId, userId);
    }

    @Override
    public Optional<Activity> getById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlGetById, new ActivityRowMapper(), id));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Activity> insert(Activity activity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, activity.getName());
                ps.setString(2, activity.getDescription());
                ps.setInt(3, activity.getUserId());
                ps.setTimestamp(4, activity.getCreationTime());
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
        return getById((Integer) keyHolder.getKeys().get("activity_id"));
    }

    @Override
    public Optional<Activity> update(Activity activity) {
        try {
            jdbcTemplate.execute(sqlUpdate, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(2, activity.getName());
                ps.setString(3, activity.getDescription());
                ps.setInt(4, activity.getUserId());
                ps.setTimestamp(5, activity.getCreationTime());
                return ps.execute();
            });
        } catch (DataAccessException e) {
            return Optional.empty();
        }
        return getById(activity.getActivityId());
    }

    @Override
    public boolean delete(int id) {
        Optional<Activity> optionalActivity = getById(id);
        if (optionalActivity.isPresent()) {
            return jdbcTemplate.update(sqlDelete, id) == 1;
        }
        return false;
    }
}
