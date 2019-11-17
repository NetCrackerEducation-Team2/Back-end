package com.netcraker.repositories;

import com.netcraker.model.BookOverview;

import java.util.List;
import java.util.Optional;

public interface BookOverviewRepository extends BaseOptionalRepository<BookOverview> {
    List<BookOverview> getByBook(int bookId);
    Optional<BookOverview> getPublishedByBook(int bookId);
}
