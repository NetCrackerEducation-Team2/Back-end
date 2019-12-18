package com.netcraker.services.impl;

import com.netcraker.model.Activity;
import com.netcraker.model.Book;
import com.netcraker.model.BookReview;
import com.netcraker.model.Page;
import com.netcraker.model.constants.NotificationTypeMessage;
import com.netcraker.model.constants.NotificationTypeName;
import com.netcraker.model.constants.TableName;
import com.netcraker.repositories.BookReviewRepository;
import com.netcraker.services.*;
import com.netcraker.services.events.DataBaseChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BookReviewServiceImp implements BookReviewService {
    private final BookReviewRepository bookReviewRepo;
    private final PageService pageService;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificationService notificationService;
    private final ActivityService activityService;
    private final BookService bookService;
    private final UserService userService;

    @Transactional
    @Override
    public Optional<BookReview> createBookReview(BookReview bookReview) {
        // inserting book review
        Optional<BookReview> inserted = bookReviewRepo.insert(bookReview);
        BookReview insertedBookReview = inserted.orElseThrow(InternalError::new);
        // updating book rating
        Book book = bookService.getBookById(insertedBookReview.getBookId()).orElseThrow(InternalError::new);
        book.setRateSum(book.getRateSum() + insertedBookReview.getRating());
        book.setVotersCount(book.getVotersCount() + 1);
        bookService.update(book).orElseThrow(InternalError::new);
        eventPublisher.publishEvent(new DataBaseChangeEvent<>(TableName.BOOK_REVIEWS, bookReview.getUserId()));
        notificationService.sendNotification(NotificationTypeName.BOOK_REVIEWS, NotificationTypeMessage.CREATE_BOOK_REVIEWS, insertedBookReview);

        // posting corresponding activity

        activityService.saveActivity(
                Activity.builder()
                        .addBookReviewActivity(
                                insertedBookReview,
                                bookService.getBookById(bookReview.getBookId()).orElseThrow(NoSuchElementException::new),
                                userService.findByUserId(bookReview.getUserId())
                        ).build());
        return inserted;
    }

    @Override
    public Optional<BookReview> updateBookReview(BookReview bookReview) {
        return bookReviewRepo.update(bookReview);
    }

    @Override
    public double getAverageRating(int bookId) {
        return bookReviewRepo.getAverageRating(bookId);
    }

    @Override
    public boolean delete(int bookId) {
        return bookReviewRepo.delete(bookId);
    }

    @Override
    public Optional<BookReview> getById(int bookReviewId) {
        return bookReviewRepo.getById(bookReviewId);
    }

    @Override
    public Page<BookReview> getPage(int bookId, int page, int pageSize) {
        int pages = pageService.getPagesCount(
                bookReviewRepo.countByUserIdBookId(null, bookId), pageSize);

        List<BookReview> list = bookReviewRepo.getPage(bookId, pageSize, page * pageSize - pageSize);
        return new Page<>(page, pages, pageSize, list);
    }

    @Override
    public Page<BookReview> getBookReviewsPagination(int page, int pageSize) {
        int total = bookReviewRepo.getCount();
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        List<BookReview> bookReviews = bookReviewRepo.getBookReviews(pageSize,offset);
        bookReviews.forEach(bookReviewRepo::loadReferences);
        return new Page<>(currentPage, pagesCount, bookReviews);
    }

    @Override
    public void publishBookReview(int id) {
        bookReviewRepo.publish(id);
    }

    @Override
    public void unpublishBookReview(int id) {
        bookReviewRepo.unpublish(id);
    }

}
