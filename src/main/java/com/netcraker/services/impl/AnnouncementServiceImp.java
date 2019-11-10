package com.netcraker.services.impl;

import com.netcraker.model.Announcement;
import com.netcraker.model.Page;
import com.netcraker.repositories.AnnouncementRepository;
import com.netcraker.services.AnnouncementService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnnouncementServiceImp implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementServiceImp(AnnouncementRepository announcementRepository) {
        Assert.notNull(announcementRepository, "AnnouncementRepository must not be null!");
        this.announcementRepository = announcementRepository;
    }

    @Override
    public Page<Announcement> getAnnouncements(int page) {
        int count = announcementRepository.getCount();
        ArrayList<Announcement> list = (ArrayList<Announcement>) announcementRepository.getAnnouncements(5,0);
        Page<Announcement> announcementPage = new Page(count, 10, page, list);
        return announcementPage;
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
