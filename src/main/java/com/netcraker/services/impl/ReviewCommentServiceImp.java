package com.netcraker.services.impl;

import com.netcraker.model.Page;
import com.netcraker.model.ReviewComment;
import com.netcraker.model.constants.TableName;
import com.netcraker.repositories.ReviewCommentRepository;
import com.netcraker.services.PageService;
import com.netcraker.services.ReviewCommentService;
import com.netcraker.services.events.DataBaseChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewCommentServiceImp implements ReviewCommentService {
    private final ReviewCommentRepository commentRepo;
    private final PageService pageService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Optional<ReviewComment> createReviewComment(ReviewComment comment) {
        eventPublisher.publishEvent(new DataBaseChangeEvent<>(TableName.REVIEW_COMMENTS, comment.getUserId()));
        return commentRepo.insert(comment);
    }

    @Override
    public Optional<ReviewComment> updateReviewComment(ReviewComment comment) {
        return commentRepo.update(comment);
    }

    @Override
    public Optional<ReviewComment> getById(int bookReviewId) {
        return commentRepo.getById(bookReviewId);
    }

    @Override
    public boolean delete(int bookId) {
        return commentRepo.delete(bookId);
    }

    @Override
    public Page<ReviewComment> getPage(int bookReviewId, int page, int pageSize) {
        int pages = pageService.getPagesCount(
                commentRepo.countByBookReviewId(bookReviewId), pageSize);

        List<ReviewComment> list = commentRepo.getPage(bookReviewId, pageSize, page * pageSize - pageSize);
        return new Page<>(page, pages, pageSize, list);
    }
}
