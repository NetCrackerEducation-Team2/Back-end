package com.netcraker.repositories;

import com.netcraker.model.Book;
import com.netcraker.model.BookFilteringParam;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public interface BookRepository extends BaseOptionalRepository<Book> {
    void loadReferences(Book book);
    int countFiltered(HashMap<BookFilteringParam, Object> filteringParams);
    List<Book> getFiltered(HashMap<BookFilteringParam, Object> filteringParams, int size, int offset);
    Optional<Book> getBySlug(String slug);
}