package com.netcraker.controllers;

import com.netcraker.model.Page;
import com.netcraker.model.UserBook;
import com.netcraker.model.UserBookFilteringParam;
import com.netcraker.services.UserBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;

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

    @GetMapping("/getFilteredUserBook")
    public ResponseEntity<Page<UserBook>> getFilteredUsersBook(
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestParam int userId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) Integer authorId,
            @RequestParam(required = false) Boolean readMark,
            @RequestParam(required = false) Boolean favoriteMark,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        HashMap<UserBookFilteringParam, Object> map = new HashMap<>();
        map.put(UserBookFilteringParam.USER_ID, userId);
        map.put(UserBookFilteringParam.TITLE, title);
        map.put(UserBookFilteringParam.GENRE, genreId);
        map.put(UserBookFilteringParam.AUTHOR, authorId);
        map.put(UserBookFilteringParam.ANNOUNCEMENT_DATE, date);
        if (readMark != null) {
            map.put(UserBookFilteringParam.DONT_SEARCH_BY_READ_MARK, false);
            map.put(UserBookFilteringParam.READ_MARK, readMark);
        } else {
            map.put(UserBookFilteringParam.DONT_SEARCH_BY_READ_MARK, true);
        }

        if (favoriteMark != null) {
            map.put(UserBookFilteringParam.DONT_SEARCH_BY_FAVORITE_MARK, false);
            map.put(UserBookFilteringParam.FAVORITE_MARK, favoriteMark);
        } else {
            map.put(UserBookFilteringParam.DONT_SEARCH_BY_FAVORITE_MARK, true);
        }

        Page<UserBook> pagination = userBookService.getFilteredBooksPagination(map, page, pageSize);
        return ResponseEntity.ok().body(pagination);
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
