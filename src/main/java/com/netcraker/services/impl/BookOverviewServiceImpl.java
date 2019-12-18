package com.netcraker.services.impl;

import com.netcraker.model.*;
import com.netcraker.model.constants.TableName;
import com.netcraker.repositories.BookOverviewRepository;
import com.netcraker.repositories.BookRepository;
import com.netcraker.services.*;
import com.netcraker.services.events.DataBaseChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookOverviewServiceImpl implements BookOverviewService {
    private final ApplicationEventPublisher eventPublisher;
    private final BookOverviewRepository bookOverviewRepository;
    private final PageService pageService;
    private final ActivityService activityService;
    private final BookRepository bookRepository;
    private final UserService userService;

    @Override
    public Page<BookOverview> getBookOverviewsByBook(int bookId, int page, int pageSize) {
        int total = bookOverviewRepository.countByBook(bookId);
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        List<BookOverview> bookOverviews = bookOverviewRepository.getByBook(bookId, pageSize, offset);
        bookOverviews.forEach(bookOverviewRepository::loadReferences);
        return new Page<>(currentPage, pagesCount, pageSize, bookOverviews);
    }
    @Override
    public Page<BookOverview> getBookOverviewsPagination(int page, int pageSize) {
        int total = bookOverviewRepository.getCount();
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        List<BookOverview> bookOverviews = bookOverviewRepository.getBookOverviews(pageSize,offset);
        bookOverviews.forEach(bookOverviewRepository::loadReferences);
        return new Page<>(currentPage, pagesCount, bookOverviews);
    }

    @Override
    public Optional<BookOverview> getPublishedBookOverviewByBook(int bookId) {
        Optional<BookOverview> optionalBookOverview =  bookOverviewRepository.getPublishedByBook(bookId);
        optionalBookOverview.ifPresent(bookOverviewRepository::loadReferences);
        return optionalBookOverview;
    }

    @Override
    public Optional<BookOverview> addBookOverview(BookOverview bookOverview) {
        final Optional<BookOverview> inserted = bookOverviewRepository.insert(bookOverview);
        // inserting corresponding activity
        BookOverview insertedBookOverview = inserted.orElseThrow(InternalError::new);
        User user = userService.findByUserId(bookOverview.getUserId());
        Book book = bookRepository.getById(insertedBookOverview.getBookId()).orElseThrow(InternalError::new);
        activityService.saveActivity(Activity.builder().createBookOverviewActivity(book, user).build());
        // posting event
        eventPublisher.publishEvent(new DataBaseChangeEvent<>(TableName.BOOK_OVERVIEWS,bookOverview.getUserId()));
        return inserted;
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
