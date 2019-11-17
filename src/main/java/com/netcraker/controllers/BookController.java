package com.netcraker.controllers;

import com.netcraker.exceptions.CreationException;
import com.netcraker.model.Book;
import com.netcraker.model.BookFilteringParam;
import com.netcraker.model.Page;
import com.netcraker.services.BookService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping({"/api"})
@CrossOrigin(methods={RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookController {

    private final @NonNull BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<Page<Book>> getBooksPage(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) Integer authorId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        HashMap<BookFilteringParam, Object> map = new HashMap<>();
        map.put(BookFilteringParam.TITLE, title);
        map.put(BookFilteringParam.GENRE, genreId);
        map.put(BookFilteringParam.AUTHOR, authorId);
        map.put(BookFilteringParam.ANNOUNCEMENT_DATE, date);
        Page<Book> pagination = bookService.getFilteredBooksPagination(map, page, pageSize);
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }

    @GetMapping("/book/download")
    public void downloadBook(@RequestParam String fileName, HttpServletResponse response){
        bookService.downloadBook(fileName, response);
    }

    @GetMapping("/book/{slug}")
    public ResponseEntity<Book> getBookBySlug(@PathVariable String slug){
        return ResponseEntity.ok().body(bookService.getBookBySlug(slug)
                .orElseThrow(() -> new CreationException("Cannot find book by slug")));
    }
}
