package com.netcraker.services;

import com.netcraker.model.Announcement;
import com.netcraker.model.Book;
import com.netcraker.model.BookOverview;
import com.netcraker.model.Page;

import java.util.List;
import java.util.Optional;

public interface BookOverviewService {
    Page<BookOverview> getBookOverviewsByBook(int bookId, int page, int pageSize);
    Optional<BookOverview> getPublishedBookOverviewByBook(int bookId);
    Optional<BookOverview> addBookOverview(BookOverview bookOverview);
    Optional<BookOverview> updateBookOverview(BookOverview bookOverview);
    boolean deleteBookOverview(int bookOverviewId);
    public void publishBookOverview(int id);
    public void unpublishBookOverview(int id);
}
