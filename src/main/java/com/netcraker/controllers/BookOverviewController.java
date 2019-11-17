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

    @GetMapping("/byBook")
    public ResponseEntity<Page<BookOverview>> getBookOverviewsByBook(
            @RequestParam int bookId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int pageSize){
        Page<BookOverview> pagination = bookOverviewService.getBookOverviewsByBook(bookId, page, pageSize);
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }

    @GetMapping("/publishedByBook")
    public ResponseEntity<BookOverview> getPublishedBookOverviewByBook(
            @RequestParam int bookId){
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

    @DeleteMapping("{bookOverviewId}")
    public ResponseEntity<?> deleteBookOverview(@PathVariable int bookOverviewId) {
        return ResponseEntity.ok().body(bookOverviewService.deleteBookOverview(bookOverviewId));
    }

}
