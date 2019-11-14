package com.netcraker.services.impl;

import com.netcraker.model.BookReview;
import com.netcraker.model.Page;
import com.netcraker.repositories.BookReviewRepository;
import com.netcraker.services.BookReviewService;

import com.netcraker.services.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BookReviewServiceImpl implements BookReviewService {
    private final BookReviewRepository bookReviewRepo;

    @Override
    public Optional<BookReview> createBookReview(BookReview bookReview) {
        return bookReviewRepo.insert(Optional.ofNullable(bookReview));
    }

    @Override
    public Optional<BookReview> updateBookReview(BookReview bookReview) {
        return bookReviewRepo.update(Optional.ofNullable(bookReview));
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
    public List<BookReview> getPage(int bookId, int page, int pageSize) {
        return bookReviewRepo.getPage(bookId, page, pageSize);
    }
}
