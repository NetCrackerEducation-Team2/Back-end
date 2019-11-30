package com.netcraker.repositories;

import com.netcraker.model.Book;

import java.util.List;

public interface BooksSelectionRepository {
    void insert(int count);
    void clear();
    List<Book> select();
}
