package com.netcraker.services.impl;

import com.netcraker.model.User;
import com.netcraker.repositories.FriendRepository;
import com.netcraker.services.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendsService {
    private final FriendRepository friendRepository;
    @Override
    public List<User> getFriends(int userId) {
        return friendRepository.getFriendsList(userId);
    }
}
