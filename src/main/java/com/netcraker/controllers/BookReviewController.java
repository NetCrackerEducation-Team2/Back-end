package com.netcraker.controllers;

import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.BookReview;
import com.netcraker.model.Page;
import com.netcraker.services.BookReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping({"/api/book-review"})
@CrossOrigin(methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class BookReviewController {
    private final BookReviewService bookReviewService;

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
    public ResponseEntity<List<BookReview>> getBookReview(
            @PathVariable("bookId") int bookId,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "1") int pageSize) {
        return ResponseEntity.ok().body(bookReviewService.getPage(bookId, pageNo, pageSize));
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
}
