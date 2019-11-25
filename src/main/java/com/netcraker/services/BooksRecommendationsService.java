package com.netcraker.services;

import com.netcraker.model.Book;
import com.netcraker.model.Page;

public interface BooksRecommendationsService {
    void prepareBooksRecommendations(int userId, int count);
    Page<Book> getBooksRecommendations(int userId, int page, int pageSize);
}
