package com.netcraker.repositories;

import com.netcraker.model.Announcement;
import java.util.List;

public interface AnnouncementRepository extends GenericRepository<Announcement> {
    void publish(int id);
    void unpublish(int id);
    List<Announcement> getAll();
    List<Announcement> getAnnouncements(int limit, int offset);
    int getCount();
    int getPublishedCount();
    List<Announcement> getPublishedAnnouncements(int pageSize, int offset);
}
