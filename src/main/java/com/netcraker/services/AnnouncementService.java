package com.netcraker.services;

import com.netcraker.model.Announcement;
import com.netcraker.model.Page;

import java.util.List;

public interface AnnouncementService {
    Page<Announcement> getAnnouncements(int page);
    Announcement getAnnouncementById(int id);
    Announcement addAnnouncement(Announcement announcement);
    Announcement updateAnnouncement(Announcement announcement);
    boolean deleteAnnouncement(int id);
    Page<Announcement> getPaginations(int page, int count);
}
