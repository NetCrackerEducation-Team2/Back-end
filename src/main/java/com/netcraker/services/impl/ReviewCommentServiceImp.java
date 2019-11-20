package com.netcraker.services.impl;

import com.netcraker.model.Page;
import com.netcraker.model.ReviewComment;
import com.netcraker.repositories.ReviewCommentRepository;
import com.netcraker.services.PageService;
import com.netcraker.services.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewCommentServiceImp implements ReviewCommentService {
    private final ReviewCommentRepository commentRepo;
    private final PageService pageService;

    @Override
    public Optional<ReviewComment> createReviewComment(ReviewComment comment) {
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
