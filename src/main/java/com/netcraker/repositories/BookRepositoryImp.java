package com.netcraker.repositories;

import com.netcraker.model.Book;
import com.netcraker.model.mapper.BookRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PropertySource("classpath:sqlQueries.properties")
public class BookRepositoryImp implements BookRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${books.getById}")
    private String sqlGetById;
    @Value("${books.insert}")
    private String sqlInsert;
    @Value("${books.update}")
    private String sqlUpdate;
    @Value("${books.delete}")
    private String sqlDelete;
    @Value("${books.getByName}")
    private String sqlGetByName;
    @Value("${books.getByGenre}")
    private String sqlGetByGenre;
    @Value("${books.getByAuthor}")
    private String sqlGetByAuthor;
    @Value("${books.getByAnnouncementDate}")
    private String sqlGetAnnouncementDate;
    @Value("${books.addGenre}")
    private String sqlAddGenre;
    @Value("${books.addAuthor}")
    private String sqlAddAuthor;

    @Override
    public List<Book> getByName(String name, int size, int offset) {
        return jdbcTemplate.query(sqlGetByName, new BookRowMapper(), name, size, offset);
    }

    @Override
    public List<Book> getByAuthor(int authorId, int size, int offset) {
        return jdbcTemplate.query(sqlGetByName, new BookRowMapper(), authorId, size, offset);
    }

    @Override
    public List<Book> getByGenre(int genreId, int size, int offset) {
        return jdbcTemplate.query(sqlGetByName, new BookRowMapper(), genreId, size, offset);
    }

    @Override
    public List<Book> getByAnnouncementDate(LocalDateTime date, int size, int offset) {
        return jdbcTemplate.query(sqlGetByName, new BookRowMapper(), Timestamp.valueOf(date), size, offset);
    }

    @Override
    public boolean addGenre(int bookId, int genreId) {
        return jdbcTemplate.execute(sqlAddGenre, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setInt(1, bookId);
            ps.setInt(2, genreId);
            return ps.execute();
        });
    }

    @Override
    public boolean addAuthor(int bookId, int authorId) {
        return jdbcTemplate.execute(sqlAddAuthor, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setInt(1, bookId);
            ps.setInt(2, authorId);
            return ps.execute();
        });
    }

    @Override
    public Book getById(int id) {
        return jdbcTemplate.queryForObject(sqlGetById, new BookRowMapper(), id);
    }

    @Override
    public boolean insert(Book entity) {
        return jdbcTemplate.execute(sqlInsert, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setString(1, entity.getTitle());
            ps.setInt(2, entity.getIsbn());
            ps.setDate(3, Date.valueOf(entity.getRelease()));
            ps.setInt(4, entity.getPages());
            ps.setString(5, entity.getFilePath());
            ps.setString(6, entity.getPhotoPath());
            ps.setString(7, entity.getPublishingHouse());
            ps.setInt(8, entity.getRateSum());
            ps.setInt(9, entity.getVotersCount());
            return ps.execute();
        });
    }

    @Override
    public boolean update(Book entity) {
        return jdbcTemplate.execute(sqlUpdate, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setString(1, entity.getTitle());
            ps.setInt(2, entity.getIsbn());
            ps.setDate(3, Date.valueOf(entity.getRelease()));
            ps.setInt(4, entity.getPages());
            ps.setString(5, entity.getFilePath());
            ps.setString(6, entity.getPhotoPath());
            ps.setString(7, entity.getPublishingHouse());
            ps.setInt(8, entity.getRateSum());
            ps.setInt(9, entity.getVotersCount());
            ps.setInt(10, entity.getBookId());
            return ps.execute();
        });
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.execute(sqlDelete, (PreparedStatementCallback<Boolean>) ps -> ps.execute());
    }
}
