package com.netcraker.services;

import com.netcraker.model.Book;
import com.netcraker.model.BookFilteringParam;
import com.netcraker.model.Page;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

public interface BookService {
      Page<Book> getFilteredBooksPagination(HashMap<BookFilteringParam, Object> filteringParams, int page, int pageSize);
      void downloadBook(String fileName, HttpServletResponse response);
      Optional<Book> getBookById(int bookId);
      Optional<Book> getBookBySlug(String slug);
}
