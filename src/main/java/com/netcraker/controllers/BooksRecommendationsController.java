package com.netcraker.controllers;

import com.netcraker.model.Book;
import com.netcraker.model.Page;
import com.netcraker.services.BooksRecommendationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/books-recommendations"})
@CrossOrigin(methods={RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor
public class BooksRecommendationsController {

    private final BooksRecommendationsService booksRecommendationsService;

    @PostMapping("/prepare/{userId}/{count}")
    public ResponseEntity<?> prepareBooksRecommendations(@PathVariable int userId,
                                            @PathVariable int count){
        booksRecommendationsService.prepareBooksRecommendations(userId, count);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<Page<Book>> getBooksRecommendations(@PathVariable int userId,
                                                              @RequestParam int page,
                                                              @RequestParam int pageSize){
        Page<Book> pagination = booksRecommendationsService.getBooksRecommendations(userId, page, pageSize);
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }
}
