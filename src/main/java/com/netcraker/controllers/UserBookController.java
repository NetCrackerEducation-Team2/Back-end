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
@CrossOrigin(methods={RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor
public class UserBookController {

    @Autowired
    private UserBookService userBookService;

    @GetMapping("/get-page-by-user/{userId}")
    public ResponseEntity<Page<UserBook>> getUsersBooksPageByUser(
            @PathVariable("userId") int userId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        return ResponseEntity.ok().body(userBookService.getPage(userId, page, pageSize));
    }

}
