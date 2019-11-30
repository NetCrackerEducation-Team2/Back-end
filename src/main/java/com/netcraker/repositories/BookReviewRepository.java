package com.netcraker.repositories;

import com.netcraker.model.BookReview;

import java.util.List;

public interface BookReviewRepository extends BaseOptionalRepository<BookReview> {
    double getAverageRating(int bookId);
    int countByUserIdBookId(Integer userId, Integer bookId);
    List<BookReview> getPage(int bookId, int pageSize, int offset);
    List<BookReview> getBookReviews(int pageSize, int offset);
    int getCount();
    void publish(int id);
    void unpublish(int id);
}
