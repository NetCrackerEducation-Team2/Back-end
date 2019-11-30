package com.netcraker.repositories;

import com.netcraker.model.BookOverview;

import java.util.List;
import java.util.Optional;

public interface BookOverviewRepository extends BaseOptionalRepository<BookOverview> {
    void loadReferences(BookOverview bookOverview);
    int countByBook(int bookId);
    List<BookOverview> getByBook(int bookId, int size, int offset);
    List<BookOverview> getBookOverviews(int size, int offset);
    Optional<BookOverview> getPublishedByBook(int bookId);
    int getCount();
    void publish(int id);
    void unpublish(int id);
}
