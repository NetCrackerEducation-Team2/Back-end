package com.netcraker.repositories.impl;

import com.netcraker.model.Announcement;
import com.netcraker.model.mapper.AnnouncementRowMapper;
import com.netcraker.repositories.AnnouncementRepository;
import com.netcraker.repositories.GenericRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
//@RequiredArgsConstructor
public class AnnouncementRepositoryImp extends GenericRepositoryImp<Announcement> implements AnnouncementRepository {
    //private final JdbcTemplate jdbcTemplate;
    private final AnnouncementRowMapper announcementRowMapper;

    @Value("${announcements.getById}")
    private String sqlGetById;
    @Value("${announcements.insert}")
    private String sqlInsert;
    @Value("${announcements.update}")
    private String sqlUpdate;
    @Value("${announcements.delete}")
    private String sqlDelete;
    @Value("${announcements.select}")
    private String sqlGetAnnouncements;
    @Value("${announcements.count}")
    private String sqlGetCount;
    @Value("${announcements.publish}")
    private String sqlPublish;
    @Value("${announcements.unPublish}")
    private String sqlUnpublish;
    @Value("${announcements.countPublished}")
    private String sqlGetPublishAnnouncementsCount;
    @Value("${announcements.selectAllPublished}")
    private String sqlGetPublishedAnnouncements;

    public AnnouncementRepositoryImp(AnnouncementRowMapper announcementRowMapper, JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.announcementRowMapper = announcementRowMapper;
    }

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
    public int getPublishedCount() {
        return jdbcTemplate.queryForObject(sqlGetPublishAnnouncementsCount, int.class);
    }

    @Override
    protected RowMapper<Announcement> getRowMapper() {
        return announcementRowMapper;
    }


    @Override
    protected String getSqlGetByIdQuery() {
        return sqlGetById;
    }

    @Override
    protected String getSqlInsertQuery() {
        return sqlInsert;
    }

    @Override
    protected String getSqlUpdateQuery() {
        return sqlUpdate;
    }

    @Override
    protected String getSqlDeleteQuery() {
        return sqlDelete;
    }

    @Override
    protected String getSqlCountQuery() {
        return sqlGetCount;
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

    @Override
    protected int setParams(Announcement entity, PreparedStatement ps, int firstParamIndex) {
        try {
            int curParamIndex = firstParamIndex;
            ps.setString(curParamIndex++, entity.getTitle());
            ps.setString(curParamIndex++, entity.getDescription());
            ps.setInt(curParamIndex++, entity.getUserId());
            return curParamIndex;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
