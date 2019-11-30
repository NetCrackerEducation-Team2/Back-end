package com.netcraker.repositories.impl;

import com.netcraker.model.Book;
import com.netcraker.model.mapper.BookRowMapper;
import com.netcraker.repositories.BooksSelectionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class BooksSelectionRepositoryImp implements BooksSelectionRepository {
    private static final Logger logger = LoggerFactory.getLogger(BooksSelectionRepositoryImp.class);
    private final JdbcTemplate jdbcTemplate;

    @Value("${booksSelection.insert}")
    private String sqlInsert;
    @Value("${booksSelection.clear}")
    private String sqlClear;
    @Value("${booksSelection.select}")
    private String sqlSelect;

    @Override
    public void insert(int count) {
        try {
            jdbcTemplate.execute(sqlInsert, (PreparedStatementCallback<Boolean>) ps -> {
                ps.setInt(1, count);
                return ps.execute();
            });
        }catch (DataAccessException e){
            logger.info("BooksSelection::insert. Stack trace: ");
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        try {
            jdbcTemplate.execute(sqlClear);
        }catch (DataAccessException e){
            logger.info("BooksSelection::clear. Stack trace: ");
            e.printStackTrace();
        }
    }

    @Override
    public List<Book> select() {
        return jdbcTemplate.query(sqlSelect, new BookRowMapper());
    }
}
