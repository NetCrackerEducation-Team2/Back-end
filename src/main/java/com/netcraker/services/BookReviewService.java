package com.netcraker.services;

import com.netcraker.model.BookReview;
import com.netcraker.model.Page;

import java.util.Optional;

public interface BookReviewService {
    Optional<BookReview> createBookReview(BookReview bookReview);
    Optional<BookReview> updateBookReview(BookReview bookReview);
    Optional<BookReview> getById(int bookReviewId);
    Page<BookReview> getBookReviewsPagination(int page, int pageSize);
    boolean delete(int bookId);
    Page<BookReview> getPage(int bookId, int page, int pageSize);
    double getAverageRating(int bookId);
    void publishBookReview(int id);
    void unpublishBookReview(int id);
}
