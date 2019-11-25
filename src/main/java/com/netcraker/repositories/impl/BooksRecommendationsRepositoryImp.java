package com.netcraker.repositories.impl;

import com.netcraker.model.Book;
import com.netcraker.model.mapper.BookRowMapper;
import com.netcraker.repositories.BooksRecommendationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class BooksRecommendationsRepositoryImp implements BooksRecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${booksRecommendations.count}")
    private String sqlCount;
    @Value("${booksRecommendations.select}")
    private String sqlSelect;
    @Value("${booksRecommendations.insert}")
    private String sqlInsert;
    @Value("${booksRecommendations.clear}")
    private String sqlClear;

    @Override
    public int count() {
        return jdbcTemplate.queryForObject(sqlCount, int.class);
    }

    @Override
    public List<Book> select(int size, int offset) {
        return jdbcTemplate.query(sqlSelect, new BookRowMapper(), size, offset);
    }

    @Override
    public void insert(List<Book> books) {
        try {
            this.jdbcTemplate.batchUpdate(sqlInsert, new BatchPreparedStatementSetter() {

                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, books.get(i).getBookId());
                    ps.setString(2, books.get(i).getTitle());
                    ps.setLong(3, books.get(i).getIsbn());
                    ps.setDate(4, Date.valueOf(books.get(i).getRelease()));
                    ps.setInt(5, books.get(i).getPages());
                    ps.setString(6, books.get(i).getFilePath());
                    ps.setString(7, books.get(i).getPhotoPath());
                    ps.setString(8, books.get(i).getPublishingHouse());
                    ps.setInt(9, books.get(i).getRateSum());
                    ps.setInt(10, books.get(i).getVotersCount());
                    ps.setString(11, books.get(i).getSlug());
                }

                public int getBatchSize() {
                    return books.size();
                }
            });
        }catch (DataAccessException e){
            System.out.println("BooksRecommendations::insert. Stack trace: ");
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        try {
            jdbcTemplate.execute(sqlClear);
        }catch (DataAccessException e){
            System.out.println("BooksRecommendations::clear. Stack trace: ");
            e.printStackTrace();
        }
    }
}
