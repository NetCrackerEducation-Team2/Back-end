package com.netcraker.repositories;

import com.netcraker.model.*;
import com.netcraker.model.mapper.BookRowMapper;
import io.jsonwebtoken.lang.Assert;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookRepositoryImp implements BookRepository {

    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull GenreRepository genreRepository;
    private final @NonNull AuthorRepository authorRepository;
    private final @NonNull BookGenreRepository bookGenreRepository;
    private final @NonNull BookAuthorRepository bookAuthorRepository;

    @Value("${books.getById}")
    private String sqlGetById;
    @Value("${books.insert}")
    private String sqlInsert;
    @Value("${books.update}")
    private String sqlUpdate;
    @Value("${books.delete}")
    private String sqlDelete;
    @Value("${books.countFiltered}")
    private String sqlCountFiltered;
    @Value("${books.getFiltered}")
    private String sqlGetFiltered;

    @Override
    public Book getById(int id) {
        return jdbcTemplate.queryForObject(sqlGetById,
                new BookRowMapper(genreRepository, authorRepository), id);
    }

    @Override
    public Book insert(Book entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getTitle());
            ps.setInt(2, entity.getIsbn());
            ps.setDate(3, Date.valueOf(entity.getRelease()));
            ps.setInt(4, entity.getPages());
            ps.setString(5, entity.getFilePath());
            ps.setString(6, entity.getPhotoPath());
            ps.setString(7, entity.getPublishingHouse());
            ps.setInt(8, entity.getRateSum());
            ps.setInt(9, entity.getVotersCount());
            ps.setString(10, entity.getSlug());
            return ps;
        }, keyHolder);
        return getById((Integer) keyHolder.getKeys().get("book_id"));
    }

    @Override
    public Book update(Book entity) {
        jdbcTemplate.execute(sqlUpdate, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setString(1, entity.getTitle());
            ps.setInt(2, entity.getIsbn());
            ps.setDate(3, Date.valueOf(entity.getRelease()));
            ps.setInt(4, entity.getPages());
            ps.setString(5, entity.getFilePath());
            ps.setString(6, entity.getPhotoPath());
            ps.setString(7, entity.getPublishingHouse());
            ps.setInt(8, entity.getRateSum());
            ps.setInt(9, entity.getVotersCount());
            ps.setString(10, entity.getSlug());
            ps.setInt(11, entity.getBookId());
            return ps.execute();
        });
        return getById(entity.getBookId());
    }

    @Override
    public boolean delete(int id) {
        Book book = getById(id);
        List<Author> authors = book.getAuthors();
        List<Genre> genres = book.getGenres();
        authors.forEach(author -> bookAuthorRepository.delete(id, author.getAuthorId()));
        genres.forEach(genre -> bookGenreRepository.delete(id, genre.getGenreId()));
        return jdbcTemplate.execute(sqlDelete, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setInt(1, id);
            return ps.execute();
        });
    }

    @Override
    public int countFiltered(HashMap<BookFilteringParam, Object> filteringParams) {
        checkBookFilteringParams(filteringParams);
        List params = getBookFilteringParams(filteringParams);
        return jdbcTemplate.queryForObject(sqlCountFiltered, params.toArray(), int.class);
    }

    @Override
    public List<Book> getFiltered(HashMap<BookFilteringParam, Object> filteringParams, int size, int offset) {
        checkBookFilteringParams(filteringParams);
        List<Object> params = getBookFilteringParams(filteringParams);
        params.add(size);
        params.add(offset);
        return jdbcTemplate.query(sqlGetFiltered, params.toArray(), new BookRowMapper(genreRepository, authorRepository));
    }

    private void checkBookFilteringParams(HashMap<BookFilteringParam, Object> filteringParams){
        if(!checkBookFilteringParamsTypes(filteringParams)){
            throw new IllegalArgumentException("One or more filtering params have incorrect types");
        }
        if(filteringParams.entrySet().size() != BookFilteringParam.values().length){
            throw new IllegalArgumentException("Illegal number of filtering params");
        }
    }

    private boolean checkBookFilteringParamsTypes(HashMap<BookFilteringParam, Object> filteringParams){
        return filteringParams.entrySet().stream().allMatch((entry) ->
                entry.getValue() == null || entry.getKey().getClazz().isInstance(entry.getValue()));
    }

    private List<Object> getBookFilteringParams(HashMap<BookFilteringParam, Object> filteringParams){
        LocalDate localDate = (LocalDate) filteringParams.get(BookFilteringParam.ANNOUNCEMENT_DATE);
        Date date = localDate == null ? null : Date.valueOf(localDate);
        Object[] params = new Object[]{
                filteringParams.get(BookFilteringParam.TITLE),
                filteringParams.get(BookFilteringParam.GENRE),
                filteringParams.get(BookFilteringParam.AUTHOR),
                date};
        List<Object> list = new ArrayList<>();
        Collections.addAll(list, params);
        return list;
    }
}