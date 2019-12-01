package com.netcraker.services;

import com.netcraker.model.Book;
import com.netcraker.model.Page;

import java.util.List;

public interface BooksRecommendationsService {
    void prepareBooksRecommendations(int userId, int count);
    List<Page<Book>> getBooksRecommendations(int userId, int pageSize);
}
