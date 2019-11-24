package com.netcraker.services;

import com.netcraker.model.Announcement;
import com.netcraker.model.Page;

import java.util.List;
import java.util.Optional;

public interface AnnouncementService {
    Page<Announcement> getAnnouncementsPagination(int page, int pageSize);
    Optional<Announcement> getAnnouncementById(int id);
    Optional<Announcement> addAnnouncement(Announcement announcement);
    Optional<Announcement> updateAnnouncement(Announcement announcement);
    boolean deleteAnnouncement(int id);
    Page<Announcement> getPaginations(int page, int count);
    Page<Announcement> getPublishAnnouncementsPagination(int page, int pageSize);
}
