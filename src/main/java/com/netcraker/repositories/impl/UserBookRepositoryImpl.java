package com.netcraker.repositories.impl;

import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.UserBook;
import com.netcraker.model.UserBookFilteringParam;
import com.netcraker.model.mapper.UserBookRowMapper;
import com.netcraker.repositories.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Date;
import java.util.*;

@Repository
@RequiredArgsConstructor
@PropertySource("classpath:sqlQueries.properties")
public class UserBookRepositoryImpl implements UserBookRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(UserBookRepositoryImpl.class);

    @Value("${user_book.getById}")
    private String sqlGetById;

    @Value("${user_book.insert}")
    private String sqlInsert;

    @Value("${user_book.update}")
    private String sqlUpdate;

    @Value("${user_book.delete}")
    private String sqlDelete;

    @Value("${user_book.getPage}")
    private String sqlGetPage;

    @Value("${user_book.countByUserId}")
    private String sqlCountByUserId;

    @Value("${user_book.getByUserIdAndBookId}")
    private String sqlGetByUserIdAndBookId;

    @Value("${user_book.getFiltered}")
    private String sqlGetFiltered;

    @Value("${user_book.countFiltered}")
    private String sqlCountFiltered;

    @Override
    public Optional<UserBook> getById(int id) {
        Object[] params = {id};
        List<UserBook> usersBook = jdbcTemplate.query(sqlGetById, params, new UserBookRowMapper());
        return usersBook.isEmpty() ? Optional.empty() : Optional.of(usersBook.get(0));
    }

    @Override
    public Optional<UserBook> findByUserAndBook(int userId, int bookId) {
        Object[] params = { userId, bookId };
        List<UserBook> usersBook = jdbcTemplate.query(sqlGetByUserIdAndBookId, params, new UserBookRowMapper());
        return usersBook.isEmpty() ? Optional.empty() : Optional.of(usersBook.get(0));
    }

    @Override
    public Optional<UserBook> insert(UserBook entity) {
        KeyHolder keyHolder;

        try {
            keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                int paramInd = 0;
                ps.setInt(++paramInd, entity.getBookId());
                ps.setInt(++paramInd, entity.getUserId());
                ps.setBoolean(++paramInd, entity.getFavoriteMark());
                ps.setBoolean(++paramInd, entity.getReadMark());
                ps.setDate(++paramInd, new java.sql.Date(new java.util.Date().getTime()));
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            logger.info("UserBook::insert entity: " + entity + ". Stack trace: " +
                    Arrays.toString(e.getStackTrace()));
            return Optional.empty();
        }
        return getById((Integer) Objects.requireNonNull(keyHolder.getKeys()).get("users_books_id"));
    }

    @Override
    public Optional<UserBook> update(UserBook entity) {
        Object[] params = {
                entity.getBookId(),
                entity.getUserId(),
                entity.getFavoriteMark(),
                entity.getReadMark(),
                entity.getCreationTime(),
                entity.getUserBookId()
        };
        int changedRowsCount = jdbcTemplate.update(sqlUpdate, params);

        if (changedRowsCount == 0)
            throw new UpdateException("User's book is not found!");

        return getById(entity.getUserId());
    }

    @Override
    public boolean delete(int id) {
        Object[] params = {id};
        return jdbcTemplate.update(sqlDelete, params) == 1;
    }

    public List<UserBook> getPage(int userId, int pageSize, int offset) {
        Object[] params = {userId, pageSize, offset};
        return jdbcTemplate.query(sqlGetPage, params, new UserBookRowMapper());
    }

    @Override
    public int countByUserId(int userId) {
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sqlCountByUserId, Integer.class, userId));
    }

    @Override
    public List<UserBook> getFiltered(HashMap<UserBookFilteringParam, Object> filteringParams, int size, int offset) {
        checkBookFilteringParams(filteringParams);
        List<Object> params = getBookFilteringParams(filteringParams);
        params.add(size);
        params.add(offset);
        return jdbcTemplate.query(sqlGetFiltered, params.toArray(), new UserBookRowMapper());
    }

    @Override
    public int countFiltered(HashMap<UserBookFilteringParam, Object> filteringParams) {
        checkBookFilteringParams(filteringParams);
        List params = getBookFilteringParams(filteringParams);
        return jdbcTemplate.queryForObject(sqlCountFiltered, params.toArray(), int.class);
    }

    private void checkBookFilteringParams(HashMap<UserBookFilteringParam, Object> filteringParams){
        if(!checkUserBookFilteringParamsTypes(filteringParams)){
            throw new IllegalArgumentException("One or more filtering params have incorrect types");
        }
        if(filteringParams.entrySet().size() != UserBookFilteringParam.values().length){
            throw new IllegalArgumentException("Illegal number of filtering params");
        }
    }

    private boolean checkUserBookFilteringParamsTypes(HashMap<UserBookFilteringParam, Object> filteringParams){
        return filteringParams.entrySet().stream().allMatch((entry) ->
                entry.getValue() == null || entry.getKey().getClazz().isInstance(entry.getValue()));
    }

    private List<Object> getBookFilteringParams(HashMap<UserBookFilteringParam, Object> filteringParams){
        LocalDate localDate = (LocalDate) filteringParams.get(UserBookFilteringParam.ANNOUNCEMENT_DATE);
        Date date = localDate == null ? null : Date.valueOf(localDate);
        Object[] params = new Object[]{
                filteringParams.get(UserBookFilteringParam.USER_ID),
                filteringParams.get(UserBookFilteringParam.DONT_SEARCH_BY_READ_MARK),
                filteringParams.get(UserBookFilteringParam.READ_MARK),
                filteringParams.get(UserBookFilteringParam.DONT_SEARCH_BY_FAVORITE_MARK),
                filteringParams.get(UserBookFilteringParam.FAVORITE_MARK),
                filteringParams.get(UserBookFilteringParam.TITLE),
                filteringParams.get(UserBookFilteringParam.GENRE),
                filteringParams.get(UserBookFilteringParam.AUTHOR),
                date};
        List<Object> list = new ArrayList<>();
        Collections.addAll(list, params);
        return list;
    }
}
