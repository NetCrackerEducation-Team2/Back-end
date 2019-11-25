package com.netcraker.services;

import com.netcraker.model.User;

import java.util.List;

public interface FriendsService {
    List<User> getFriends(int userId);
}
