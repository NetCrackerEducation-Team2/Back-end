package com.netcraker.repositories.impl;

import com.netcraker.model.BookOverview;
import com.netcraker.repositories.BookOverviewRepository;

import java.util.List;
import java.util.Optional;

public class BookOverviewRepositoryImp implements BookOverviewRepository {
    
    @Override
    public List<BookOverview> getByBook(int bookId) {
        return null;
    }

    @Override
    public Optional<BookOverview> getPublishedByBook(int bookId) {
        return Optional.empty();
    }

    @Override
    public Optional<BookOverview> getById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<BookOverview> insert(BookOverview entity) {
        return Optional.empty();
    }

    @Override
    public Optional<BookOverview> update(BookOverview entity) {
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
