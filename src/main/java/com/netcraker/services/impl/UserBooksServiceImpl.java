package com.netcraker.services.impl;

import com.netcraker.exceptions.FindException;
import com.netcraker.model.Page;
import com.netcraker.model.UserBook;
import com.netcraker.repositories.UserBookRepository;
import com.netcraker.services.PageService;
import com.netcraker.services.UserBookService;
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

    @Override
    public Page<UserBook> getPage(int userId, int page, int pageSize) {
        int pages = pageService.getPagesCount(userBookRepository.countByUserId(userId), pageSize);

        List<UserBook> list = userBookRepository.getPage(userId, pageSize, page * pageSize - pageSize);
        return new Page<>(page, pages, pageSize, list);
    }

    @Override
    public UserBook addUsersBook(UserBook newUserBook) {
        UserBook res = userBookRepository.(newUserBook);
        return userBookRepository
                .getById(usersBookId)
                .orElseThrow(() -> new FindException("UserBook is not found"));

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

    @Override
    public UserBook setFavoriteMark(int usersBookId, boolean value) {
        UserBook userBook = userBookRepository
                .getById(usersBookId)
                .orElseThrow(() -> new FindException("UserBook is not found"));

        userBook.setFavoriteMark(value);
        userBookRepository.update(userBook);
        return userBookRepository
                .getById(usersBookId)
                .orElseThrow(() -> new FindException("UserBook is not found"));
    }
}
