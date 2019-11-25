package com.netcraker.services.impl;

import com.netcraker.model.BookReview;
import com.netcraker.model.Page;
import com.netcraker.model.constants.TableName;
import com.netcraker.repositories.BookReviewRepository;
import com.netcraker.services.BookReviewService;

import com.netcraker.services.PageService;
import com.netcraker.services.events.DataBaseChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BookReviewServiceImp implements BookReviewService {
    private final BookReviewRepository bookReviewRepo;
    private final PageService pageService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Optional<BookReview> createBookReview(BookReview bookReview) {
        eventPublisher.publishEvent(new DataBaseChangeEvent<>(TableName.BOOK_REVIEWS, bookReview.getUserId()));
        return bookReviewRepo.insert(bookReview);
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
}
