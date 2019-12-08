package com.netcraker.controllers;

import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.FindException;
import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.BookReview;
import com.netcraker.model.Page;
import com.netcraker.model.ReviewComment;
import com.netcraker.services.BookReviewService;
import com.netcraker.services.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping({"/api/book-review"})
@CrossOrigin(methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RequiredArgsConstructor
public class BookReviewController {
    private final BookReviewService bookReviewService;
    private final ReviewCommentService reviewCommentService;

    @PostMapping
    public ResponseEntity<BookReview> createBookReview(@RequestBody @Validated BookReview bookReview,
                                                       BindingResult result) {
        if (result.hasErrors()) {
            throw new CreationException("Cannot create book review");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookReviewService.createBookReview(bookReview)
                        .orElseThrow(() -> new CreationException("Cannot create book review")));
    }

    @GetMapping("all/{bookId}")
    public ResponseEntity<Page<BookReview>> getBookReview(
            @PathVariable("bookId") int bookId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        Page<BookReview> res = bookReviewService.getPage(bookId, page, pageSize);
        System.out.println(res);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping
    public ResponseEntity<BookReview> updateBookReview(@RequestBody BookReview bookReview) {
        return ResponseEntity.ok()
                .body(bookReviewService.updateBookReview(bookReview)
                        .orElseThrow(() -> new UpdateException("Cannot update book review")));
    }

    @GetMapping("rating/{bookId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable int bookId) {
        return ResponseEntity.ok().body(bookReviewService.getAverageRating(bookId));
    }

    @DeleteMapping("{bookReviewId}")
    public ResponseEntity<?> deleteBookReview(@PathVariable int bookReviewId) {
        return ResponseEntity.ok().body(bookReviewService.delete(bookReviewId));
    }


    @PostMapping("/comment")
    public ResponseEntity<?> createReviewComment(@RequestBody @Validated ReviewComment comment,
                                                 BindingResult result) {
        if (result.hasErrors()) {
            throw new CreationException("Cannot create review comment");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewCommentService.createReviewComment(comment)
                        .orElseThrow(() -> new CreationException("Cannot create review comment")));
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<?> getReviewComment(@PathVariable int commentId) {
        return ResponseEntity.ok(reviewCommentService.getById(commentId)
                .orElseThrow(() -> new FindException("Cannot find review comment")));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteReviewComment(@PathVariable int commentId) {
        return ResponseEntity.ok(reviewCommentService.delete(commentId));
    }

    @PutMapping("/comment")
    public ResponseEntity<?> updateReviewComment(@RequestBody @Validated ReviewComment comment,
                                                 BindingResult result) {
        if (result.hasErrors()) {
            throw new UpdateException("Cannot create review comment");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(reviewCommentService.updateReviewComment(comment)
                        .orElseThrow(() -> new UpdateException("Cannot create review comment")));
    }

    @PutMapping("/publish/{id}")
    public ResponseEntity<?> publishBookReview(@PathVariable int id){
        bookReviewService.publishBookReview(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/unpublish/{id}")
    public ResponseEntity<?> unpublishBookReview(@PathVariable int id){
        bookReviewService.unpublishBookReview(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }




    @GetMapping("/comment/all/{bookReviewId}")
    public ResponseEntity<Page<ReviewComment>> getReviewComments(
            @PathVariable("bookReviewId") int bookReviewId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        return ResponseEntity.ok().body(reviewCommentService.getPage(bookReviewId, page, pageSize));
    }

    @PostMapping("/comment/add")
    public ResponseEntity<ReviewComment> addReviewComment(@RequestBody ReviewComment newComment) {
        newComment.setCreationTime(LocalDateTime.now());
        return ResponseEntity.ok().body(reviewCommentService.createReviewComment(newComment)
                .orElseThrow(() -> new FindException("Cannot find review comment")));
    }
}
