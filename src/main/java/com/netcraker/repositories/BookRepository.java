package com.netcraker.repositories;

import com.netcraker.model.Book;
import com.netcraker.model.BookFilteringParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;


public interface BookRepository extends BaseRepository<Book> {
    int countFiltered(HashMap<BookFilteringParam, Object> filteringParams);
    List<Book> getFiltered(HashMap<BookFilteringParam, Object> filteringParams, int size, int offset);
}
