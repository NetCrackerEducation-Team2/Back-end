package com.netcraker.services;

import com.netcraker.model.Announcement;
import com.netcraker.model.Page;

import java.util.List;

public interface AnnouncementService {
    List<Announcement> getAllAnnouncements();
    Announcement getAnnouncementById();
    boolean addAnnouncement(Announcement announcement);
    boolean updateAnnouncement(Announcement announcement);
    boolean deleteAnnouncement(int id);
    Page<Announcement> getPaginations(int page, int count);
}
