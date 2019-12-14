package com.netcraker.controllers;

import com.netcraker.model.Page;
import com.netcraker.model.UserBook;
import com.netcraker.services.UserBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/users-book"})
@CrossOrigin(methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor
public class UserBookController {

    @Autowired
    private UserBookService userBookService;

    @GetMapping("/getUserBook")
    public ResponseEntity<UserBook> getUsersBook(
            @RequestParam(value = "book") int bookId,
            @RequestParam(value = "user") int userId
    ) {
        return ResponseEntity.ok().body(userBookService.getUserBook(bookId, userId));
    }

    @GetMapping("/get-page-by-user/{userId}")
    public ResponseEntity<Page<UserBook>> getUsersBooksPageByUser(
            @PathVariable("userId") int userId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        return ResponseEntity.ok().body(userBookService.getPage(userId, page, pageSize));
    }

    @PutMapping("/addUserBook")
    public ResponseEntity<UserBook> addBookToUsersList(
            @RequestParam(value = "book") int bookId,
            @RequestParam(value = "user") int userId
    ) {
        return ResponseEntity.ok().body(userBookService.addUsersBook(bookId, userId));
    }

    @PutMapping("/deleteUserBook")
    public ResponseEntity<?> deleteBookFromUsersList(
            @RequestParam(value = "userBook") int userBook
    ) {
        userBookService.deleteUsersBook(userBook);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/markReadUserBook")
    public ResponseEntity<UserBook> markReadUserBook(
            @RequestParam(value = "userBook") int userBook,
            @RequestParam(value = "value") boolean value
    ) {
        return ResponseEntity.ok().body(userBookService.setReadMark(userBook, value));
    }

    @PutMapping("/markFavoriteUserBook")
    public ResponseEntity<UserBook> markFavoriteUserBook(
            @RequestParam(value = "userBook") int userBook,
            @RequestParam(value = "value") boolean value
    ) {
        return ResponseEntity.ok().body(userBookService.setFavoriteMark(userBook, value));
    }
}
