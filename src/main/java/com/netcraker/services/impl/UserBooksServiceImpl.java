package com.netcraker.services.impl;

import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.FindException;
import com.netcraker.model.*;
import com.netcraker.repositories.UserBookRepository;
import com.netcraker.repositories.UserRepository;
import com.netcraker.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserBooksServiceImpl implements UserBookService {
    private final PageService pageService;
    private final UserBookRepository userBookRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;
    private final BookService bookService;
    private final UserService userService;

    @Override
    public UserBook getUserBook(int bookId, int userId) {
        Optional<UserBook> item = userBookRepository.findByUserAndBook(userId, bookId);
        return item.orElse(UserBook.builder().userBookId(-1).build());
    }

    @Override
    public Page<UserBook> getFilteredBooksPagination(HashMap<UserBookFilteringParam, Object> filteringParams, int page, int pageSize) {
        int total = userBookRepository.countFiltered(filteringParams);
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        List<UserBook> userBooks = userBookRepository.getFiltered(filteringParams, pageSize, offset);
        return new Page<>(currentPage, pagesCount, pageSize, userBooks);
    }

    @Override
    public Page<UserBook> getPage(int userId, int page, int pageSize) {
        int pages = pageService.getPagesCount(userBookRepository.countByUserId(userId), pageSize);

        List<UserBook> list = userBookRepository.getPage(userId, pageSize, page * pageSize - pageSize);
        return new Page<>(page, pages, pageSize, list);
    }

    @Override
    public UserBook addUsersBook(int bookId, int userId) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new FindException("User is not found!"));

        Optional<UserBook> userBook = userBookRepository.findByUserAndBook(user.getUserId(), bookId);
        if (userBook.isPresent()) {
            return userBook.get();
        }

        UserBook newUserBook = UserBook.builder()
                .bookId(bookId)
                .userId(user.getUserId())
                .readMark(false)
                .favoriteMark(false)
                .creationTime(LocalDateTime.now())
                .build();

        Optional<UserBook> res = userBookRepository.insert(newUserBook);
        return res.orElseThrow(
                () -> new CreationException("Can't create a new instance of UserBook."));
    }

    @Override
    public void deleteUsersBook(int usersBookId) {
        userBookRepository.delete(usersBookId);
    }

    @Override
    public UserBook setReadMark(int usersBookId, boolean value) {
        UserBook userBook = userBookRepository
                .getById(usersBookId)
                .orElseThrow(() -> new FindException("UserBook is not found"));

        userBook.setReadMark(value);
        userBookRepository.update(userBook);
        return userBookRepository
                .getById(usersBookId)
                .orElseThrow(() -> new FindException("UserBook is not found"));
    }
    @Transactional
    @Override
    public UserBook setFavoriteMark(int usersBookId, boolean value) {
        UserBook userBook = userBookRepository
                .getById(usersBookId)
                .orElseThrow(() -> new FindException("UserBook is not found"));
        userBook.setFavoriteMark(value);
        userBookRepository.update(userBook);
        // posting corresponding activity
        Book book = bookService.getBookById(userBook.getBookId()).orElseThrow(InternalError::new);
        User user = userService.findByUserId(userBook.getUserId());
        activityService.saveActivity(Activity.builder().addToFavouriteActivity(book, user).build()).orElseThrow(InternalError::new);

        return userBookRepository
                .getById(usersBookId)
                .orElseThrow(() -> new FindException("UserBook is not found"));
    }
}
