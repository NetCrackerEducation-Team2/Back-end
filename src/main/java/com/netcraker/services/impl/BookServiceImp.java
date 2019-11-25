package com.netcraker.services.impl;

import com.netcraker.model.Book;
import com.netcraker.model.BookFilteringParam;
import com.netcraker.model.Page;
import com.netcraker.repositories.BookRepository;
import com.netcraker.services.BookService;
import com.netcraker.services.FileService;
import com.netcraker.services.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@PropertySource({"classpath:path.properties"})
@RequiredArgsConstructor
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;
    private final PageService pageService;
    private final FileService fileService;

    @Value("${books.contentPath}")
    private String booksContentPath;
    @Value("${books.imagePath}")
    private String booksImagePath;
    @Value("${books.defaultImageName}")
    private String booksDefaultImageName;

    @Override
    public Page<Book> getFilteredBooksPagination(HashMap<BookFilteringParam, Object> filteringParams, int page, int pageSize) {
        int total = bookRepository.countFiltered(filteringParams);
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = currentPage * pageSize;
        List<Book> books = bookRepository.getFiltered(filteringParams, pageSize, offset);
        books.forEach(bookRepository::loadReferences);
        books.forEach(this::insureBookPhoto);
        return new Page<>(currentPage, pagesCount, pageSize, books);
    }

    @Override
    public void downloadBook(String fileName, HttpServletResponse response) {
        fileService.downloadFile(booksContentPath + fileName, response);
    }

    @Override
    public Optional<Book> getBookBySlug(String slug) {
        Optional<Book> optionalBook = bookRepository.getBySlug(slug);
        optionalBook.ifPresent(bookRepository::loadReferences);
        optionalBook.ifPresent(this::insureBookPhoto);
        return optionalBook;
    }

    @Override
    public Optional<Book> getBookById(int bookId) {
        Optional<Book> optionalBook = bookRepository.getById(bookId);
        optionalBook.ifPresent(bookRepository::loadReferences);
        optionalBook.ifPresent(this::insureBookPhoto);
        return optionalBook;
    }

    private void insureBookPhoto(Book book){
        byte[] photo = fileService.getImage(booksImagePath + book.getPhotoPath());
        if(photo == null) {
            book.setPhotoPath(booksDefaultImageName);
            book.setPhoto(fileService.getImage(booksImagePath + book.getPhotoPath()));
        }else{
            book.setPhoto(photo);
        }
    }

    @Override
    public Optional<Book> createBook(Book book) {
//        book.setSlug(new Slugify().slugify(book.getTitle()));
        return bookRepository.insert(book);
    }
}
