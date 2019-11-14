package com.netcraker.repositories;

import com.netcraker.model.BookReview;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Optional;

public interface BookReviewRepository extends BaseRepository<Optional<BookReview>> {
    double getAverageRating(int bookId);
    List<BookReview> getPage(int bookId, int page, int count);
}
