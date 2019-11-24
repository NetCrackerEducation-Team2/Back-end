package com.netcraker.repositories;

import com.netcraker.model.UserBook;

import java.util.List;

public interface UserBookRepository extends BaseOptionalRepository<UserBook> {
    List<UserBook> getPage(int userId, int page, int pageSize);
}
