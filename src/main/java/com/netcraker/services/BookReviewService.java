package com.netcraker.services;

import com.netcraker.model.BookReview;

import java.util.List;
import java.util.Optional;

public interface BookReviewService {
    Optional<BookReview> createBookReview(BookReview bookReview);
    Optional<BookReview> updateBookReview(BookReview bookReview);
    Optional<BookReview> getById(int bookReviewId);
    boolean delete(int bookId);

    List<BookReview> getPage(int bookId, int page, int pageSize);
    double getAverageRating(int bookId);
}
