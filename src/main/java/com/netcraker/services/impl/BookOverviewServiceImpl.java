package com.netcraker.services.impl;

import com.netcraker.model.BookOverview;
import com.netcraker.repositories.BookOverviewRepository;
import com.netcraker.services.BookOverviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookOverviewServiceImpl implements BookOverviewService {

    private final BookOverviewRepository bookOverviewRepository;

    @Override
    public Optional<BookOverview> addBookOverview(BookOverview bookOverview) {
        return bookOverviewRepository.insert(bookOverview);
    }

    @Override
    public Optional<BookOverview> updateBookOverview(BookOverview bookOverview) {
        return bookOverviewRepository.update(bookOverview);
    }
}
