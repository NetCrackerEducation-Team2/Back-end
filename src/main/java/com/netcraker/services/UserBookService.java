package com.netcraker.services;

import com.netcraker.model.Page;
import com.netcraker.model.UserBook;

public interface UserBookService {
    Page<UserBook> getPage(int userId, int page, int pageSize);
    void setFavoriteMark(int usersBookId, boolean value);
    void setReadMark(int usersBookId, boolean value);
}
