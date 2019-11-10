package com.netcraker.repositories;

import com.netcraker.model.Announcement;

import java.util.ArrayList;
import java.util.List;

public interface AnnouncementRepository {
    boolean createAnnouncement(Announcement announcement);
    boolean updateAnnouncement(Announcement announcement);
    boolean deleteAnnouncement(int id);
    List<Announcement> getAll();
    List<Announcement> getAnnouncements(int limit, int offset);
    int getCount();
    Announcement getAnnouncementById();
    Announcement getAnnouncementByTitle();
}
