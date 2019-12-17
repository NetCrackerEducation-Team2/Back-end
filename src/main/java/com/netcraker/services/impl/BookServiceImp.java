package com.netcraker.services.impl;

import com.netcraker.exceptions.RequiresAuthenticationException;
import com.netcraker.model.*;
import com.netcraker.model.vo.SuggestBookReq;
import com.netcraker.repositories.BookRepository;
import com.netcraker.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;
    private final PageService pageService;
    private final FileService fileService;
    private final UserInfoService userInfoService;
    private final BookOverviewService bookOverviewService;

    @Override
    public Page<Book> getFilteredBooksPagination(HashMap<BookFilteringParam, Object> filteringParams, int page, int pageSize) {
        int total = bookRepository.countFiltered(filteringParams);
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        List<Book> books = bookRepository.getFiltered(filteringParams, pageSize, offset);
        books.forEach(bookRepository::loadReferences);
        return new Page<>(currentPage, pagesCount, pageSize, books);
    }

    @Override
    public void downloadBook(String path, HttpServletResponse response) {
        fileService.downloadFile(path, response);
    }

    @Override
    public Optional<Book> getBookBySlug(String slug) {
        Optional<Book> optionalBook = bookRepository.getBySlug(slug);
        optionalBook.ifPresent(bookRepository::loadReferences);
        return optionalBook;
    }

    @Override
    public Optional<Book> getBookById(int bookId) {
        Optional<Book> optionalBook = bookRepository.getById(bookId);
        optionalBook.ifPresent(bookRepository::loadReferences);
        return optionalBook;
    }

    @Override
    public Optional<Book> createBook(Book book) {
        return bookRepository.insert(book);
    }

    @Transactional
    @Override
    public Book suggestBook(SuggestBookReq suggestBookRequest) {
        User currentUser = userInfoService.getCurrentUser().orElseThrow(RequiresAuthenticationException::new);

        // saving book
        Book book = this.bookRepository.insert(suggestBookRequest.convertToBook()).orElseThrow(InternalError::new);
        // saving book overview
        BookOverview bookOverview = suggestBookRequest.convertToBookOverview();
        bookOverview.setBook(book);
        bookOverview.setBookId(book.getBookId());
        bookOverview.setUser(currentUser);
        bookOverview.setUserId(currentUser.getUserId());
        bookOverview = bookOverviewService.addBookOverview(bookOverview).orElseThrow(InternalError::new);
        return book;
    }
}
