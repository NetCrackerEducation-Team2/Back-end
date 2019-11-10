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

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PropertySource({"classpath:view.properties", "classpath:path.properties"})
public class BookServiceImp implements BookService {

    private final @NonNull BookRepository bookRepository;
    private final @NonNull PageService pageService;
    private final @NonNull FileService fileService;

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
