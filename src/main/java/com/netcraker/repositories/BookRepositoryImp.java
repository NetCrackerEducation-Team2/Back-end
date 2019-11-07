package com.netcraker.repositories;

import com.netcraker.model.Book;
import com.netcraker.model.Page;
import com.netcraker.model.mapper.BookRowMapper;
import lombok.NonNull;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PropertySource("classpath:sqlQueries.properties")
public class BookRepositoryImp implements BookRepository {

    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull GenreRepository genreRepository;
    private final @NonNull AuthorRepository authorRepository;

    @Value("${books.countAll}")
    private String sqlCountAll;
    @Value("${books.getAll}")
    private String sqlGetAll;
    @Value("${books.getById}")
    private String sqlGetById;
    @Value("${books.insert}")
    private String sqlInsert;
    @Value("${books.update}")
    private String sqlUpdate;
    @Value("${books.delete}")
    private String sqlDelete;
    @Value("${books.countByName}")
    private String sqlCountByName;
    @Value("${books.getByName}")
    private String sqlGetByName;
    @Value("${books.countByGenre}")
    private String sqlCountByGenre;
    @Value("${books.getByGenre}")
    private String sqlGetByGenre;
    @Value("${books.countByAuthor}")
    private String sqlCountByAuthor;
    @Value("${books.getByAuthor}")
    private String sqlGetByAuthor;
    @Value("${books.countByAnnouncementDate}")
    private String sqlCountByAnnouncementDate;
    @Value("${books.getByAnnouncementDate}")
    private String sqlGetAnnouncementDate;

    @Override
    public int countAll() {
        return jdbcTemplate.queryForObject(sqlCountAll, int.class);
    }

    @Override
    public List<Book> getAll(int size, int offset) {
        return jdbcTemplate.query(sqlGetAll,
                new BookRowMapper(genreRepository, authorRepository), size, offset);
    }

    @Override
    public int countByName(String name) {
        return jdbcTemplate.queryForObject(sqlCountByName, int.class);
    }

    @Override
    public List<Book> getByName(String name, int size, int offset) {
        return jdbcTemplate.query(sqlGetByName,
                new BookRowMapper(genreRepository, authorRepository), name, size, offset);
    }

    @Override
    public int countByAuthor(int authorId) {
        return jdbcTemplate.queryForObject(sqlCountByAuthor, int.class, authorId);
    }

    @Override
    public List<Book> getByAuthor(int authorId, int size, int offset) {
        return jdbcTemplate.query(sqlGetByName,
                new BookRowMapper(genreRepository, authorRepository), authorId, size, offset);
    }

    @Override
    public int countByGenre(int genreId) {
        return jdbcTemplate.queryForObject(sqlCountByGenre, int.class, genreId);
    }

    @Override
    public List<Book> getByGenre(int genreId, int size, int offset) {
        return jdbcTemplate.query(sqlGetByName,
                new BookRowMapper(genreRepository, authorRepository), genreId, size, offset);
    }

    @Override
    public int countByAnnouncementDate(LocalDate date) {
        return jdbcTemplate.queryForObject(sqlCountByAnnouncementDate, int.class, date);
    }

    @Override
    public List<Book> getByAnnouncementDate(LocalDate date, int size, int offset) {
        return jdbcTemplate.query(sqlGetByName,
                new BookRowMapper(genreRepository, authorRepository), Date.valueOf(date), size, offset);
    }

    @Override
    public Book getById(int id) {
        return jdbcTemplate.queryForObject(sqlGetById,
                new BookRowMapper(genreRepository, authorRepository), id);
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
