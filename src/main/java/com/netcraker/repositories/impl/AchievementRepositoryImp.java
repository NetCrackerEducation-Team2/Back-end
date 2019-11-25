package com.netcraker.repositories.impl;

import com.netcraker.model.Achievement;
import com.netcraker.model.constants.TableName;
import com.netcraker.model.mapper.AchievementRowMapper;
import com.netcraker.repositories.AchievementRepository;
import com.netcraker.services.events.DataBaseChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class AchievementRepositoryImp implements AchievementRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${achievements.getById}")
    private String sqlGetById;
    @Value("${achievements.insert}")
    private String sqlInsert;
    @Value("${achievements.update}")
    private String sqlUpdate;
    @Value("${achievements.delete}")
    private String sqlDelete;
    @Value("${achievements.getByName}")
    private String sqlGetByName;
    @Value("${achievements.getByTableName}")
    private String sqlGetByTableName;

    @Override
    public Optional<Achievement> getById(int id) {
        List<Achievement> result = jdbcTemplate.query(sqlGetById, new Object[]{id}, new AchievementRowMapper());
        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }

    @Override
    public List<Achievement> getByTableName(TableName tableName) {
        return jdbcTemplate.query(sqlGetByTableName, new Object[]{tableName.getTableName()}, new AchievementRowMapper());
    }

    @Override
    public Optional<Achievement> insert(Achievement entity) {
        System.out.println(entity);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getRequirement());
                ps.setString(3, entity.getTableName().getTableName()); // can change to entity.getTableName().name()
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            System.out.println("Achievements::insert entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
        return getById((Integer) keyHolder.getKeys().get("achievement_id"));
    }

    @Override
    public Optional<Achievement> update(Achievement entity) {
        try {
            jdbcTemplate.execute(Objects.requireNonNull(sqlUpdate), (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getRequirement());
                return ps.execute();
            });
            return getById(entity.getAchievementId());
        } catch (DataAccessException e) {
            System.out.println("Achievements::update entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(sqlDelete, id) == 1;
    }

    @Override
    public Optional<Achievement> getByName(String name) {
        List<Achievement> result = jdbcTemplate.query(sqlGetByName, new Object[]{name}, new AchievementRowMapper());
        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }
}
