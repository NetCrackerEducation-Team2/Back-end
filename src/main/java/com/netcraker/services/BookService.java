package com.netcraker.services;

import com.netcraker.model.Book;
import com.netcraker.model.Page;

import java.time.LocalDate;

public interface BookService {
    Page<Book> getBooksPagination(int page);
    Page<Book> getBooksByNamePagination(String name, int page);
    Page<Book> getBooksByGenrePagination(int genreId, int page);
    Page<Book> getBooksByAuthorPagination(int authorId, int page);
    Page<Book> getBooksByAnnouncementDatePagination(LocalDate date, int page);
}
