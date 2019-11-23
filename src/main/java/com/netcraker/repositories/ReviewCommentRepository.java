package com.netcraker.repositories;

import com.netcraker.model.BookReview;
import com.netcraker.model.ReviewComment;

import java.util.List;

public interface ReviewCommentRepository extends BaseOptionalRepository<ReviewComment>{
    List<ReviewComment> getPage(int bookReviewId, int pageSize, int offset);
    int countByBookReviewId(int commentId);
}
