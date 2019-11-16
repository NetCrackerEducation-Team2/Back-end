package com.netcraker.repositories.impl;

import com.netcraker.model.Announcement;
import com.netcraker.model.mapper.AnnouncementRowMapper;
import com.netcraker.model.mapper.BookReviewRowMapper;
import com.netcraker.repositories.AnnouncementRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public class AnnouncementRepositoryImp implements AnnouncementRepository {

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
        private String sqlUnPublish;



        @Override
    public List<Announcement> getAll() {
        return jdbcTemplate.query(sqlGetAnnouncements, new Object[]{ 5, 0}, announcementRowMapper);
    }

    @Override
    public List<Announcement> getAnnouncements(int limit, int offset) {
        return jdbcTemplate.query(sqlGetAnnouncements, new Object[]{ limit, offset}, announcementRowMapper);
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
    public Optional<Announcement> getById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlGetById, announcementRowMapper, id));
        } catch (DataAccessException e) {
            System.out.println("Announcement::getById id: " + id + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
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
                ps.setInt(4, entity.getBookId());
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            System.out.println("Announcement::insert entity: " + entity + ". Stack trace: ");
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
            System.out.println("AnnouncementReview::update entity: " + entity + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.execute(sqlDelete, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setInt(1, id);
            return ps.execute();
        });
    }
}
