package com.netcraker.repositories;

import com.netcraker.model.Announcement;
import com.netcraker.model.mapper.AnnouncementRowMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnouncementRepositoryImp implements AnnouncementRepository {

    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull Environment environment;

    @Override
    public boolean createAnnouncement(Announcement announcement) {
        return false;
    }

    @Override
    public boolean updateAnnouncement(Announcement announcement) {
        return false;
    }

    @Override
    public boolean deleteAnnouncement(int id) {
        return false;
    }

    @Override
    public List<Announcement> getAll() {
        //String announcementSQLQuery = "SELECT * FROM announcements LIMIT ? OFFSET ?";
        String announcementsQuery = environment.getProperty("announcements.select");
        return jdbcTemplate.query(announcementsQuery, new Object[]{ 5, 0}, new AnnouncementRowMapper());
    }

    @Override
    public List<Announcement> getAnnouncements(int limit, int offset) {
        String announcementsQuery = environment.getProperty("announcements.select");
        return jdbcTemplate.query(announcementsQuery, new Object[]{ limit, offset}, new AnnouncementRowMapper());
    }

    @Override
    public int getCount() {
        String announcementsQuery = environment.getProperty("announcements.count");
        return jdbcTemplate.queryForObject(announcementsQuery, int.class);
    }

    @Override
    public Announcement getAnnouncementById() {
        return null;
    }

    @Override
    public Announcement getAnnouncementByTitle() {
        return null;
    }
}
