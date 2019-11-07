package com.netcraker.services.impl;

import com.netcraker.model.Book;
import com.netcraker.model.Page;
import com.netcraker.repositories.BookRepository;
import com.netcraker.services.BookService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PropertySource("classpath:view.properties")
public class BookServiceImp implements BookService {

    private final @NonNull BookRepository bookRepository;

    @Value("${books.pageSize}")
    private int pageSize;

    @Override
    public Page<Book> getBooksPagination(int page) {
        int total = bookRepository.countAll();
        int pagesCount = (int) Math.ceil((double) total / pageSize);
        int currentPage = Math.min(pagesCount, Math.max(1, page));
        int offset = (currentPage - 1) * pageSize;
        ArrayList<Book> books = new ArrayList<>(bookRepository.getAll(pageSize, offset));
        return new Page<>(currentPage, pagesCount, books);
    }

    @Override
    public Page<Book> getBooksByNamePagination(String name, int page) {
        int total = bookRepository.countByName(name);
        int pagesCount = (int) Math.ceil((double) total / pageSize);
        int currentPage = Math.min(pagesCount, Math.max(1, page));
        int offset = (currentPage - 1) * pageSize;
        ArrayList<Book> books = new ArrayList<>(bookRepository.getByName(name, pageSize, offset));
        return new Page<>(currentPage, pagesCount, books);
    }

    @Override
    public Page<Book> getBooksByGenrePagination(int genreId, int page) {
        int total = bookRepository.countByGenre(genreId);
        int pagesCount = (int) Math.ceil((double) total / pageSize);
        int currentPage = Math.min(pagesCount, Math.max(1, page));
        int offset = (currentPage - 1) * pageSize;
        ArrayList<Book> books = new ArrayList<>(bookRepository.getByGenre(genreId, pageSize, offset));
        return new Page<>(currentPage, pagesCount, books);
    }

    @Override
    public Page<Book> getBooksByAuthorPagination(int authorId, int page) {
        int total = bookRepository.countByAuthor(authorId);
        int pagesCount = (int) Math.ceil((double) total / pageSize);
        int currentPage = Math.min(pagesCount, Math.max(1, page));
        int offset = (currentPage - 1) * pageSize;
        ArrayList<Book> books = new ArrayList<>(bookRepository.getByAuthor(authorId, pageSize, offset));
        return new Page<>(currentPage, pagesCount, books);
    }

    @Override
    public Page<Book> getBooksByAnnouncementDatePagination(LocalDate date, int page) {
        int total = bookRepository.countByAnnouncementDate(date);
        int pagesCount = (int) Math.ceil((double) total / pageSize);
        int currentPage = Math.min(pagesCount, Math.max(1, page));
        int offset = (currentPage - 1) * pageSize;
        ArrayList<Book> books = new ArrayList<>(bookRepository.getByAnnouncementDate(date, pageSize, offset));
        return new Page<>(currentPage, pagesCount, books);
    }
}
