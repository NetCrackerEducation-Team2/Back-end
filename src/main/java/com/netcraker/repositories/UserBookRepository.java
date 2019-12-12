package com.netcraker.repositories;

import com.netcraker.model.UserBook;

import java.util.List;
import java.util.Optional;

public interface UserBookRepository extends BaseOptionalRepository<UserBook> {
    List<UserBook> getPage(int userId, int page, int pageSize);
    int countByUserId(int userId);
    Optional<UserBook> findByUserAndBook(int userId, int bookId);
}
