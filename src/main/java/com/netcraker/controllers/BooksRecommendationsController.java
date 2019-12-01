package com.netcraker.controllers;

import com.netcraker.model.Book;
import com.netcraker.model.Page;
import com.netcraker.services.BooksRecommendationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/books-recommendations"})
@CrossOrigin(methods={RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@RequiredArgsConstructor
public class BooksRecommendationsController {

    private final BooksRecommendationsService booksRecommendationsService;

    @PutMapping("/prepare/{userId}")
    public ResponseEntity<?> prepareBooksRecommendations(@PathVariable int userId,
                                                         @RequestParam int count){
        booksRecommendationsService.prepareBooksRecommendations(userId, count);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<List<Page<Book>>> getBooksRecommendations(@PathVariable int userId,
                                                                    @RequestParam int pageSize){
        List<Page<Book>> pagination = booksRecommendationsService.getBooksRecommendations(userId, pageSize);
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }
}
