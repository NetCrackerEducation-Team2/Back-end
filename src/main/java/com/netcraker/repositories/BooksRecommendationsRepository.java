package com.netcraker.repositories;

import com.netcraker.model.Book;

import java.time.LocalDateTime;
import java.util.List;

public interface BooksRecommendationsRepository {
    int count(int userId);
    List<Book> get(int userId);
    LocalDateTime getLatestUpdateTime(int userId);
    void insert(int userId, List<Book> books);
    void clear(int userId);
}
