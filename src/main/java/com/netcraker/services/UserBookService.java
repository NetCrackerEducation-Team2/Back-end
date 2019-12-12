package com.netcraker.services;

import com.netcraker.model.Page;
import com.netcraker.model.UserBook;

public interface UserBookService {
    Page<UserBook> getPage(int userId, int page, int pageSize);
    UserBook addUsersBook(UserBook newUserBook);
    void deleteUsersBook(int usersBookId);
    UserBook setFavoriteMark(int usersBookId, boolean value);
    UserBook setReadMark(int usersBookId, boolean value);
}
