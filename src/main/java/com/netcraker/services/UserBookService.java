package com.netcraker.services;

import com.netcraker.model.Page;
import com.netcraker.model.UserBook;
import com.netcraker.model.UserBookFilteringParam;

import java.util.HashMap;

public interface UserBookService {
    UserBook getUserBook(int bookId, int userId);
    Page<UserBook> getFilteredBooksPagination(HashMap<UserBookFilteringParam, Object> filteringParams, int page, int pageSize);
    Page<UserBook> getPage(int userId, int page, int pageSize);
    UserBook addUsersBook(int bookId, int userId);
    void deleteUsersBook(int usersBookId);
    UserBook setFavoriteMark(int usersBookId, boolean value);
    UserBook setReadMark(int usersBookId, boolean value);
}
