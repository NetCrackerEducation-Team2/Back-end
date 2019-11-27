package com.netcraker.repositories.impl;

import com.netcraker.model.Announcement;
import com.netcraker.model.mapper.AnnouncementRowMapper;
import com.netcraker.repositories.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
@RequiredArgsConstructor
public class AnnouncementRepositoryImp implements AnnouncementRepository {

    private static final Logger logger = LoggerFactory.getLogger(AnnouncementRepositoryImp.class);
    private final JdbcTemplate jdbcTemplate;
    private final AnnouncementRowMapper announcementRowMapper;

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
    private String sqlUnpublish;
    @Value("${announcements.countPublished}")
    private String sqlGetPublishAnnouncementsCount;
    @Value("${announcements.selectAllPublished}")
    private String sqlGetPublishedAnnouncements;


    @Override
    public List<Announcement> getAll() {
        return jdbcTemplate.query(sqlGetAnnouncements, new Object[]{5, 0}, announcementRowMapper);
    }

    @Override
    public List<Announcement> getAnnouncements(int limit, int offset) {
        return jdbcTemplate.query(sqlGetAnnouncements, new Object[]{limit, offset}, announcementRowMapper);
    }

    @Override
    public List<Announcement> getPublishedAnnouncements(int limit, int offset) {
        return jdbcTemplate.query(sqlGetPublishedAnnouncements, new Object[]{ limit, offset}, announcementRowMapper);
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(sqlGetAnnouncementsCount, int.class);
    }
    @Override
    public int getPublishedCount() {
        return jdbcTemplate.queryForObject(sqlGetPublishAnnouncementsCount, int.class);
    }

    @Override
    public Announcement getAnnouncementByTitle() {
        return null;
    }

    @Override
    public Optional<Announcement> getById(int id) {
        List<Announcement> announcements = jdbcTemplate.query(sqlGetById, announcementRowMapper, id);
        return announcements.isEmpty() ? Optional.empty() : Optional.of(announcements.get(0));
    }

    @Override
    public Optional<Announcement> insert(Announcement entity) {
        KeyHolder keyHolder;
        try {
            keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, entity.getTitle());
                ps.setString(2, entity.getDescription());
                ps.setInt(3, entity.getUserId());
                ps.setNull(4, Types.INTEGER);
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            logger.info("Announcement::insert entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
        return getById((Integer) keyHolder.getKeys().get("announcement_id"));
    }

    @Override
    public Optional<Announcement> update(Announcement entity) {
        try {
            jdbcTemplate.execute(Objects.requireNonNull(sqlUpdate), (PreparedStatementCallback<Boolean>) ps -> {
                ps.setString(1, entity.getTitle());
                ps.setString(2, entity.getDescription());
                ps.setInt(3, entity.getUserId());
                ps.setInt(4, entity.getBookId());       
                ps.setInt(5, entity.getAnnouncementId());
                return ps.execute();
            });
            return getById(entity.getAnnouncementId());
        } catch (DataAccessException e) {
           logger.info("Announcement::update entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.execute(sqlDelete, (PreparedStatement ps) -> {
            ps.setInt(1, id);
            return ps.execute();
        });
    }

    @Override
    public void publish(int id){
        Object[] params = {id};
        jdbcTemplate.update(sqlPublish, params);

    }

    @Override
    public void unpublish(int id){
        Object[] params = {id};
        jdbcTemplate.update(sqlUnpublish, params);

    }
}
