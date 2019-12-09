package com.netcraker.services.impl;

import com.netcraker.model.Activity;
import com.netcraker.model.Announcement;
import com.netcraker.model.Page;
import com.netcraker.model.User;
import com.netcraker.repositories.AnnouncementRepository;
import com.netcraker.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@PropertySource({"classpath:view.properties"})
@RequiredArgsConstructor
public class AnnouncementServiceImp implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final PageService pageService;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;
    private final ActivityService activityService;
    private final UserService userService;

    @Override
    public Page<Announcement> getAnnouncementsPagination(int page, int pageSize) {
        int total = announcementRepository.getCount();
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        List<Announcement> list = announcementRepository.getAnnouncements(pageSize, offset);
        return new Page<>(currentPage, pagesCount, list);
    }

    @Override
    public Page<Announcement> getPublishAnnouncementsPagination(int page, int pageSize) {
        int totalPublish = announcementRepository.getPublishedCount();
        int pagesCount = pageService.getPagesCount(totalPublish, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        List<Announcement> list = announcementRepository.getPublishedAnnouncements(pageSize, offset);
        return new Page<>(currentPage, pagesCount, list);
    }

    @Override
    public Optional<Announcement> getAnnouncementById(int id) {
        return announcementRepository.getById(id);
    }

    @Override
    @Transactional
    public Optional<Announcement> addAnnouncement(Announcement announcement) {
        // creating corresponding activity
        int announcementAuthorId = announcement.getUserId();
        User announcementCreator = userService.findByUserId(announcementAuthorId);
        activityService.saveActivity(Activity.builder().announcementActivity(announcement, announcementCreator).build());
        final Optional<Announcement> inserted = announcementRepository.insert(announcement);
        //eventPublisher.publishEvent(new DataBaseChangeEvent<>(TableName.BOOK_REVIEWS, announcement.getUserId()));
        notificationService.sendNotification(10, 12, inserted.orElse(null));
        return inserted;
    }

    @Override
    public Optional<Announcement> updateAnnouncement(Announcement announcement) {
        return announcementRepository.update(announcement);
    }

    @Override
    public void publishAnnouncement(int id) {
        announcementRepository.publish(id);
        //Announcement announcement = announcementRepository.getById(id).orElse(null);
        //notificationService.sendNotification(10, 15, announcement);

    }

    @Override
    public void unpublishAnnouncement(int id) {
        announcementRepository.unpublish(id);
    }

    @Override
    public boolean deleteAnnouncement(int id) {
        return announcementRepository.delete(id);
    }

    @Override
    public Page<Announcement> getPaginations(int page, int count) {
        return null;
    }
}
