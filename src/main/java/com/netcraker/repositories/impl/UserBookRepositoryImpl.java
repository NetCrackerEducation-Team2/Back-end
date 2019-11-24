package com.netcraker.repositories.impl;

import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.UserBook;
import com.netcraker.model.mapper.UserBookRowMapper;
import com.netcraker.repositories.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@PropertySource("classpath:sqlQueries.properties")
public class UserBookRepositoryImpl implements UserBookRepository {

    private final JdbcTemplate jdbcTemplate;

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

    @Override
    public Optional<UserBook> getById(int id) {
        Object[] params = { id };
        List<UserBook> users = jdbcTemplate.query(sqlGetById, params, new UserBookRowMapper());
        return Optional.ofNullable(users.get(0));
    }

    @Override
    public Optional<UserBook> insert(UserBook entity) {
        Object[] params = {
                entity.getUserBookId(),
                entity.getBookId(),
                entity.getUserId(),
                entity.getFavoriteMark(),
                entity.getReadMark(),
                new Timestamp(System.currentTimeMillis())
        };
        jdbcTemplate.update(sqlInsert, params);
        return getById(entity.getUserBookId());
    }

    @Override
    public Optional<UserBook> update(UserBook entity) {
        Object[] params = {
                entity.getBookId(),
                entity.getUserId(),
                entity.getFavoriteMark(),
                entity.getReadMark(),
                new Timestamp(System.currentTimeMillis()),
                entity.getUserBookId()
        };
        int changedRowsCount = jdbcTemplate.update(sqlUpdate, params);

        if (changedRowsCount == 0)
            throw new UpdateException("User's book is not found!");
        if (changedRowsCount > 1)
            throw new UpdateException("Multiple update! Only one user's book can be changed!");

        return getById(entity.getUserId());
    }

    @Override
    public boolean delete(int id) {
        Object[] params = { id };
        return jdbcTemplate.update(sqlDelete, params) == 1;
    }

    public List<UserBook> getPage(int userId, int pageSize, int offset) {
        Object[] params = { userId, pageSize, offset };
        return jdbcTemplate.query(sqlGetPage, params, new UserBookRowMapper());
    }
}
