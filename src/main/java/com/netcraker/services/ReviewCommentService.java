package com.netcraker.services;

import com.netcraker.model.Page;
import com.netcraker.model.ReviewComment;

import java.util.Optional;

public interface ReviewCommentService {
    Optional<ReviewComment> createReviewComment(ReviewComment comment);
    Optional<ReviewComment> updateReviewComment(ReviewComment comment);
    Optional<ReviewComment> getById(int bookReviewId);
    boolean delete(int bookId);
    Page<ReviewComment> getPage(int bookReviewId, int page, int pageSize);
}
