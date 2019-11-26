package com.netcraker.repositories;

import com.netcraker.model.Book;

import java.util.List;

public interface BooksRecommendationsRepository {
    int count();
    List<Book> select(int size, int offset);
    void insert(List<Book> books);
    void clear();
}
