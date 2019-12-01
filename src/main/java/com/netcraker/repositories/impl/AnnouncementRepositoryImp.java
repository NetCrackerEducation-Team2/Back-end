package com.netcraker.repositories.impl;

import com.netcraker.model.Announcement;
import com.netcraker.model.mapper.AnnouncementRowMapper;
import com.netcraker.repositories.AnnouncementRepository;
import com.netcraker.repositories.GenericRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("${classpath:sqlQueries.properties}")
@RequiredArgsConstructor
public class AnnouncementRepositoryImp implements AnnouncementRepository {
    private final JdbcTemplate jdbcTemplate;
    private final AnnouncementRowMapper announcementRowMapper;
    private final GenericRepository<Announcement, AnnouncementRowMapper> genericRepository;

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
        //return genericRepository.getById(announcementRowMapper, sqlGetById, id);
        return genericRepository.getById(Announcement.class, announcementRowMapper,  id);
    }

    @Override
    public Optional<Announcement> insert(Announcement entity) {
        /*KeyHolder keyHolder;
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
        return getById((Integer) keyHolder.getKeys().get("announcement_id"));*/
        Object[] params = {entity.getTitle(), entity.getDescription(), entity.getUserId()};
        return genericRepository.insert(entity, announcementRowMapper, params);
    }

    @Override
    public Optional<Announcement> update(Announcement entity) {
        /*try {
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
        }*/

        /*for (Field f: Announcement.class.getDeclaredFields()) {
            EntityId column = f.getAnnotation(EntityId.class);
            if (column != null)
                System.out.println(column.value());
        }*/

        Object[] params = {entity.getTitle(), entity.getDescription(), entity.getUserId(),
                entity.getAnnouncementId()};
        return genericRepository.update(entity, announcementRowMapper, params,  entity.getAnnouncementId());


    }

    @Override
    public boolean delete(int id) {
        return genericRepository.delete(sqlDelete, id);
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
