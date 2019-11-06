package com.netcraker.repositories;

import com.netcraker.model.Announcement;
import com.netcraker.model.mapper.AnnouncementRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AnnouncementRepositoryImp implements AnnouncementRepository {
    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.queries.announcements}")
    private String announcementsQuery;

    @Autowired
    public AnnouncementRepositoryImp(JdbcTemplate jdbcTemplate) {
        Assert.notNull(jdbcTemplate, "JdbcTemplate must not be null!");
        this.jdbcTemplate = jdbcTemplate;
    }

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
        //Announcement a = new Announcement(1, "ff" , " ff", 1, true, LocalDateTime.now(), 1);
        //List<Announcement> list = new ArrayList<>();
        //list.add(a);

        //String announcementSQLQuery = "SELECT * FROM announcements LIMIT ? OFFSET ?";
        return jdbcTemplate.query(announcementsQuery, new Object[]{ 5, 0}, new AnnouncementRowMapper());
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
