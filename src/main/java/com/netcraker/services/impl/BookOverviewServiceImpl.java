package com.netcraker.services.impl;

import com.netcraker.model.BookOverview;
import com.netcraker.model.Page;
import com.netcraker.repositories.BookOverviewRepository;
import com.netcraker.services.BookOverviewService;
import com.netcraker.services.PageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookOverviewServiceImpl implements BookOverviewService {

    private final @NonNull BookOverviewRepository bookOverviewRepository;
    private final @NonNull PageService pageService;

    @Override
    public Page<BookOverview> getBookOverviewsByBook(int bookId, int page, int pageSize) {
        int total = bookOverviewRepository.countByBook(bookId);
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        List<BookOverview> bookOverviews = bookOverviewRepository.getByBook(bookId, pageSize, offset);
        return new Page<>(currentPage, pagesCount, pageSize, bookOverviews);
    }

    @Override
    public Optional<BookOverview> getPublishedBookOverviewByBook(int bookId) {
        return bookOverviewRepository.getPublishedByBook(bookId);
    }

    @Override
    public Optional<BookOverview> addBookOverview(BookOverview bookOverview) {
        return bookOverviewRepository.insert(bookOverview);
    }

    @Override
    public Optional<BookOverview> updateBookOverview(BookOverview bookOverview) {
        return bookOverviewRepository.update(bookOverview);
    }

    @Override
    public boolean deleteBookOverview(int bookOverviewId) {
        return bookOverviewRepository.delete(bookOverviewId);
    }

    @Override
    public void publishBookOverview(int id) {
        bookOverviewRepository.publish(id);
    }

    @Override
    public void unpublishBookOverview(int id) {
        bookOverviewRepository.unpublish(id);
    }

}
