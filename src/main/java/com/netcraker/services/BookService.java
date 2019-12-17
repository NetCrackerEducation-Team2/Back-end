package com.netcraker.services;

import com.netcraker.model.Book;
import com.netcraker.model.BookFilteringParam;
import com.netcraker.model.Page;
import com.netcraker.model.vo.SuggestBookReq;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

public interface BookService {
    Page<Book> getFilteredBooksPagination(HashMap<BookFilteringParam, Object> filteringParams, int page, int pageSize);

    void downloadBook(String filePath, HttpServletResponse response);

    Optional<Book> createBook(Book book);

    Optional<Book> getBookById(int bookId);

    Optional<Book> getBookBySlug(String slug);

    Book suggestBook(SuggestBookReq suggestBookRequest);
}
