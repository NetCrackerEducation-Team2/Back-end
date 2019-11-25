package com.netcraker.repositories.impl;

import com.netcraker.model.User;
import com.netcraker.model.mapper.BookReviewRowMapper;
import com.netcraker.model.mapper.UserRowMapper;
import com.netcraker.repositories.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {
    private final JdbcTemplate jdbcTemplate;
    @Value("${friends.getFriendsList}")
    private String sqlGetFriendsList;
    @Override
    public List<User> getFriendsList(int userId) {
        return jdbcTemplate.query(sqlGetFriendsList, UserRowMapper.builder().build(), userId, userId);
    }
}