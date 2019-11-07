package com.netcraker.services.impl;

import com.netcraker.model.Announcement;
import com.netcraker.model.Page;
import com.netcraker.repositories.AnnouncementRepository;
import com.netcraker.services.AnnouncementService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class AnnouncementServiceImp implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementServiceImp(AnnouncementRepository announcementRepository) {
        Assert.notNull(announcementRepository, "AnnouncementRepository must not be null!");
        this.announcementRepository = announcementRepository;
    }

    @Override
    public List<Announcement> getAllAnnouncements() {

        return announcementRepository.getAll();
    }

    @Override
    public Announcement getAnnouncementById() {
        return null;
    }

    @Override
    public boolean addAnnouncement(Announcement announcement) {
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
    public Page<Announcement> getPaginations(int page, int count) {
        return null;
    }
}
