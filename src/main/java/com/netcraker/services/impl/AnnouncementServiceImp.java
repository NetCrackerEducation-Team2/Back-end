package com.netcraker.services.impl;

import com.netcraker.model.Announcement;
import com.netcraker.model.Page;
import com.netcraker.model.constants.TableName;
import com.netcraker.repositories.AnnouncementRepository;
import com.netcraker.services.AnnouncementService;
import com.netcraker.services.PageService;
import com.netcraker.services.events.DataBaseChangeEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@PropertySource({"classpath:view.properties"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnouncementServiceImp implements AnnouncementService {

    private final @NonNull AnnouncementRepository announcementRepository;
    private final @NonNull PageService pageService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Page<Announcement> getAnnouncementsPagination(int page, int pageSize) {
        int total = announcementRepository.getCount();
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        ArrayList<Announcement> list = (ArrayList<Announcement>) announcementRepository.getAnnouncements(pageSize,offset);
        return new Page<>(currentPage, pagesCount, list);
    }

    @Override
    public Optional<Announcement> getAnnouncementById(int id) {
        return announcementRepository.getById(id);
    }

    @Override
    public Optional<Announcement> addAnnouncement(Announcement announcement) {
        eventPublisher.publishEvent(new DataBaseChangeEvent<>(TableName.BOOK_REVIEWS, announcement.getUserId()));
        return announcementRepository.insert(announcement);
    }

    @Override
    public Optional<Announcement> updateAnnouncement(Announcement announcement) {
        return announcementRepository.update(announcement);
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
