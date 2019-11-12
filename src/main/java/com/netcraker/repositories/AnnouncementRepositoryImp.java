package com.netcraker.repositories;

import com.netcraker.model.Announcement;
import com.netcraker.model.mapper.AnnouncementRowMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnouncementRepositoryImp implements AnnouncementRepository {

    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull Environment environment;

    @Value("${announcements.getById}")
    private String sqlGetById;
    @Value("${announcements.select}")
    private String sqlGetAnnouncements;
    @Value("${announcements.count}")
    private String sqlGetAnnouncementsCount;
    @Value("${announcements.insert}")
    private String sqlInsert;
    @Value("${announcements.update}")
    private String sqlUpdate;
    @Value("${announcements.delete}")
    private String sqlDelete;
    @Value("${announcements.publish}")
    private String sqlPublish;
    @Value("${announcements.unPublish}")
    private String sqlUnPublish;



    @Override
    public List<Announcement> getAll() {
        return jdbcTemplate.query(sqlGetAnnouncements, new Object[]{ 5, 0}, new AnnouncementRowMapper());
    }

    @Override
    public List<Announcement> getAnnouncements(int limit, int offset) {
        return jdbcTemplate.query(sqlGetAnnouncements, new Object[]{ limit, offset}, new AnnouncementRowMapper());
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(sqlGetAnnouncementsCount, int.class);
    }

    @Override
    public Announcement getAnnouncementByTitle() {
        return null;
    }

    @Override
    public Announcement getById(int id) {
        return jdbcTemplate.queryForObject(sqlGetById,
                new AnnouncementRowMapper(), id);
    }

    @Override
    public Announcement insert(Announcement entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getTitle());
            ps.setString(2, entity.getDescription());
            ps.setInt(3, entity.getUserId());
            ps.setBoolean(4, entity.isPublished());
            ps.setInt(5, entity.getBookId());
            return ps;
        }, keyHolder);
        return getById((Integer) keyHolder.getKeys().get("announcement_id"));
    }

    @Override
    public Announcement update(Announcement entity) {
        jdbcTemplate.execute(sqlUpdate, (PreparedStatementCallback<Boolean>) ps -> {
             ps.setString(1, entity.getTitle());
             ps.setString(2, entity.getDescription());
             ps.setInt(3, entity.getUserId());
             ps.setBoolean(4, entity.isPublished());
             ps.setInt(5, entity.getBookId());
             ps.setInt(6, entity.getAnnouncementId());
             return ps.execute();
        });
        return getById(entity.getBookId());
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.execute(sqlDelete, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setInt(1, id);
            return ps.execute();
        });
    }
}
