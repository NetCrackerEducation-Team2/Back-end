package com.netcraker.repositories.impl;

import com.netcraker.model.User;
import com.netcraker.model.mapper.UserRowMapper;
import com.netcraker.repositories.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {
    private final JdbcTemplate jdbcTemplate;
    @Value("${friends.getFriendsList}")
    private String sqlGetFriendsList;
    @Value("${friends.checkFriends}")
    private String sqlCheckFriends;
    @Value("${friends.checkAwaitingFriendRequestAccept}")
    private String sqlCheckAwaitingFriendRequestAccept;
    @Value("${friends.deleteByUsersId}")
    private String sqlDeleteByUsersId;
    @Value("${friends.insert}")
    private String sqlInsertFriends;
    @Value("${friends.sqlGetFriendsPageable}")
    private String sqlGetFriendsPageable;
    @Value("${friends.getFriendsPageableCount}")
    private String sqlGetFriendsPageableCount;
    @Value("${friends.checkDeclinedFriendRequest}")
    private String sqlCheckDeclinedFriendRequest;
    @Override
    public List<User> getFriendsList(int userId) {
        return jdbcTemplate.query(sqlGetFriendsList, UserRowMapper.builder().build(), userId, userId);
    }

    @Override
    public boolean isFriends(int user1Id, int user2Id) {
        return jdbcTemplate.queryForObject(sqlCheckFriends, boolean.class, user1Id, user2Id, user2Id, user1Id);
    }

    @Override
    public boolean isAwaitingFriendRequestAccept(int sourceUserId, int targetUserId) {
        return jdbcTemplate.queryForObject(sqlCheckAwaitingFriendRequestAccept, boolean.class, sourceUserId, targetUserId);
    }

    @Override
    public boolean deleteFromFriends(int userId, int friendId) {
        int countUpdatedRows = jdbcTemplate.update(sqlDeleteByUsersId, userId, friendId, friendId, userId);
        return countUpdatedRows > 0;
    }

    @Override
    public boolean isDeclinedFriendRequest(int sourceUserId, int targetUserId) {
        return jdbcTemplate.queryForObject(sqlCheckDeclinedFriendRequest, boolean.class, sourceUserId, targetUserId);
    }

    @Override
    public void addFriends(int userId, int user2Id) {
        int affectedRows = jdbcTemplate.update(conn -> {
            try {
                PreparedStatement ps = conn.prepareStatement(sqlInsertFriends);
                ps.setInt(1, userId);
                ps.setInt(2, user2Id);
                return ps;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        if (affectedRows != 1) {
            throw new RuntimeException("Can not make friends");
        }
    }

    @Override
    public List<User> getFriendsPageable(int userId, int limit, int offset) {
        return jdbcTemplate.query(sqlGetFriendsPageable, UserRowMapper.builder().build(), userId, userId, limit, offset);
    }

    @Override
    public int getFriendsPageableCount(int userId) {
        return jdbcTemplate.queryForObject(sqlGetFriendsPageableCount, int.class, userId, userId);
    }
}