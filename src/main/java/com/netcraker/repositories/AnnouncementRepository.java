package com.netcraker.repositories;

import com.netcraker.model.Announcement;

import java.util.List;

public interface AnnouncementRepository {
    boolean createAnnouncement(Announcement announcement);
    boolean updateAnnouncement(Announcement announcement);
    boolean deleteAnnouncement(int id);
    List<Announcement> getAll();
    Announcement getAnnouncementById();
    Announcement getAnnouncementByTitle();
}
