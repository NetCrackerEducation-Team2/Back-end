package com.netcraker.controllers;

import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.Announcement;
import com.netcraker.model.BookOverview;
import com.netcraker.model.Page;
import com.netcraker.services.BookOverviewService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping({"/api/book-overviews"})
@RequiredArgsConstructor
@CrossOrigin(methods={RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class BookOverviewController {

    private final BookOverviewService bookOverviewService;

    @GetMapping("/by-book/{bookId}")
    public ResponseEntity<Page<BookOverview>> getBookOverviewsByBook(
            @PathVariable int bookId,
            @RequestParam int page,
            @RequestParam int pageSize){
        Page<BookOverview> pagination = bookOverviewService.getBookOverviewsByBook(bookId, page, pageSize);
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<BookOverview>> getBookOverviews(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int pageSize) {
        Page<BookOverview> pagination = bookOverviewService.getBookOverviewsPagination(page, pageSize);
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }
//refactoring
    @GetMapping("/published-by-book/{bookId}")
    public ResponseEntity<BookOverview> getPublishedBookOverviewByBook(
            @PathVariable int bookId){
        Optional<BookOverview> optionalBookOverview = bookOverviewService.getPublishedBookOverviewByBook(bookId);
        return new ResponseEntity<>(optionalBookOverview.orElse(null), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookOverview> createBookOverview(@RequestBody @Validated BookOverview bookOverview,
                                                           BindingResult result){
        if (result.hasErrors()) {
            throw new CreationException("Cannot create book overview");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookOverviewService.addBookOverview(bookOverview)
                        .orElseThrow(() -> new CreationException("Cannot create book overview")));
    }

    @PutMapping
    public ResponseEntity<BookOverview> updateBookOverview(@RequestBody @Validated BookOverview bookOverview){
        return ResponseEntity.ok()
                .body(bookOverviewService.updateBookOverview(bookOverview)
                        .orElseThrow(() -> new UpdateException("Cannot update book overview")));
    }

    @PutMapping("/publish/{id}")
    public ResponseEntity<?> publishBookOverview(@PathVariable int id){
        bookOverviewService.publishBookOverview(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/unpublish/{id}")
    public ResponseEntity<?> unpublishBookOverview(@PathVariable int id){
        bookOverviewService.unpublishBookOverview(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("{bookOverviewId}")
    public ResponseEntity<?> deleteBookOverview(@PathVariable int bookOverviewId) {
        return ResponseEntity.ok().body(bookOverviewService.deleteBookOverview(bookOverviewId));
    }

}
