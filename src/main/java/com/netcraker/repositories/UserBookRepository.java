package com.netcraker.repositories;

import com.netcraker.model.UserBook;
import com.netcraker.model.UserBookFilteringParam;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface UserBookRepository extends BaseOptionalRepository<UserBook> {
    List<UserBook> getPage(int userId, int page, int pageSize);
    int countByUserId(int userId);

    List<UserBook> getFiltered(HashMap<UserBookFilteringParam, Object> filteringParams, int size, int offset);
    int countFiltered(HashMap<UserBookFilteringParam, Object> filteringParams);

    Optional<UserBook> findByUserAndBook(int userId, int bookId);
}
