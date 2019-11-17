package com.netcraker.repositories.impl;

import com.netcraker.model.BookOverview;
import com.netcraker.model.mapper.BookOverviewRowMapper;
import com.netcraker.model.mapper.BookRowMapper;
import com.netcraker.repositories.BookOverviewRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookOverviewRepositoryImp implements BookOverviewRepository {

    private final @NonNull JdbcTemplate jdbcTemplate;

    @Value("${book_overviews.getById}")
    private String sqlGetById;
    @Value("${book_overview.getByBook}")
    private String sqlGetByBook;
    @Value("${book_overview.getPublishedByBook}")
    private String sqlGetPublishedByBook;
    
    @Override
    public List<BookOverview> getByBook(int bookId) {
        return jdbcTemplate.query(sqlGetByBook, new BookOverviewRowMapper(), bookId);
    }

    @Override
    public Optional<BookOverview> getPublishedByBook(int bookId) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlGetByBook,
                    new BookOverviewRowMapper(), bookId));
        }catch (DataAccessException e) {
            System.out.println("BookOverview::getPublishedByBook bookId: " + bookId + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<BookOverview> getById(int id) {
        try{
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlGetById,
                    new BookOverviewRowMapper(), id));
        }catch (DataAccessException e) {
            System.out.println("BookOverview::getById id: " + id + ". Stack trace: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<BookOverview> insert(BookOverview entity) {
        return Optional.empty();
    }

    @Override
    public Optional<BookOverview> update(BookOverview entity) {
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
