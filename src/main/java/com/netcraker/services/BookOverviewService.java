package com.netcraker.services;

import com.netcraker.model.Announcement;
import com.netcraker.model.BookOverview;

import java.util.Optional;

public interface BookOverviewService {
    Optional<BookOverview> addBookOverview(BookOverview bookOverview);
    Optional<BookOverview> updateBookOverview(BookOverview bookOverview);
}
