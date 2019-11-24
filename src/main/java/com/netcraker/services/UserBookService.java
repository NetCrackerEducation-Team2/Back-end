package com.netcraker.services;

import com.netcraker.model.Page;
import com.netcraker.model.UserBook;

public interface UserBookService {
    Page<UserBook> getPage(int userId, int page, int pageSize);
}
