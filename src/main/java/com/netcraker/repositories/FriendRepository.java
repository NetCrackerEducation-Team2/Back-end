package com.netcraker.repositories;

import com.netcraker.model.User;

import java.util.List;

public interface FriendRepository {
    List<User> getFriendsList(int userId);
}
