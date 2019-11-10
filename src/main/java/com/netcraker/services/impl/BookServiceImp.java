package com.netcraker.services.impl;

import com.netcraker.model.Book;
import com.netcraker.model.BookFilteringParam;
import com.netcraker.model.Page;
import com.netcraker.repositories.BookRepository;
import com.netcraker.services.BookService;
import com.netcraker.services.FileService;
import com.netcraker.services.PageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@PropertySource({"classpath:view.properties", "classpath:path.properties"})
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;
    private final PageService pageService;
    private final FileService fileService;

    @Autowired
    public BookServiceImp(BookRepository bookRepository, PageService pageService, FileService fileService) {
        Assert.notNull(bookRepository, "BookRepository shouldn't be null");
        Assert.notNull(pageService, "PageService shouldn't be null");
        Assert.notNull(fileService, "FileService shouldn't be null");
        this.bookRepository = bookRepository;
        this.pageService = pageService;
        this.fileService = fileService;
    }

    @Value("${books.contentPath}")
    private String booksContentPath;
    @Value("${books.imagePath}")
    private String booksImagePath;

    @Value("${books.pageSize}")
    private int pageSize;

    @Override
    public Page<Book> getFilteredBooksPagination(HashMap<BookFilteringParam, Object> filteringParams, int page) {
        int total = bookRepository.countFiltered(filteringParams);
        int pagesCount = pageService.getPagesCount(total, pageSize);
        int currentPage = pageService.getRestrictedPage(page, pagesCount);
        int offset = (currentPage - 1) * pageSize;
        ArrayList<Book> books = new ArrayList<>(bookRepository.getFiltered(filteringParams, pageSize, offset));
        books.forEach(book -> book.setPhoto(fileService.getImage(booksImagePath + book.getPhotoPath())));
        return new Page<>(currentPage, pagesCount, books);
    }

    @Override
    public void downloadBook(String fileName, HttpServletResponse response) {
        fileService.downloadFile(booksContentPath + fileName, response);
    }
}
