package com.netcraker.services.impl;

import com.netcraker.exceptions.FindException;
import com.netcraker.model.*;
import com.netcraker.repositories.UserBookRepository;
import com.netcraker.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserBooksServiceImpl implements UserBookService {
    private final PageService pageService;
    private final UserBookRepository userBookRepository;
    private final ActivityService activityService;
    private final BookService bookService;
    private final UserService userService;

    @Override
    public Page<UserBook> getPage(int userId, int page, int pageSize) {
        int pages = pageService.getPagesCount(userBookRepository.countByUserId(userId), pageSize);

        List<UserBook> list = userBookRepository.getPage(userId, pageSize, page * pageSize - pageSize);
        return new Page<>(page, pages, pageSize, list);
    }

    @Override
    public void setReadMark(int usersBookId, boolean value) {
        UserBook userBook = userBookRepository
                .getById(usersBookId)
                .orElseThrow(() -> new FindException("UserBook is not found"));

        userBook.setReadMark(value);
        userBookRepository.update(userBook);
    }
    @Transactional
    @Override
    public void setFavoriteMark(int usersBookId, boolean value) {
        UserBook userBook = userBookRepository
                .getById(usersBookId)
                .orElseThrow(() -> new FindException("UserBook is not found"));
        userBook.setFavoriteMark(value);
        userBookRepository.update(userBook);
        // posting corresponding activity
        Book book = bookService.getBookById(userBook.getBookId()).orElseThrow(InternalError::new);
        User user = userService.findByUserId(userBook.getUserId());
        activityService.saveActivity(Activity.builder().addToFavouriteActivity(book, user).build()).orElseThrow(InternalError::new);
    }
}
