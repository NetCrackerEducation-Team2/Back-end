package com.netcraker.services;

import com.netcraker.model.BookReview;
import com.netcraker.model.Page;

import java.util.Optional;

public interface BookReviewService {
    Optional<BookReview> createBookReview(BookReview bookReview);
    Optional<BookReview> updateBookReview(BookReview bookReview);
    Optional<BookReview> getById(int bookReviewId);
    boolean delete(int bookId);
    Page<BookReview> getPage(int bookId, int page, int pageSize);
    double getAverageRating(int bookId);
    public void publishBookReview(int id);
    public void unpublishBookReview(int id);
}
