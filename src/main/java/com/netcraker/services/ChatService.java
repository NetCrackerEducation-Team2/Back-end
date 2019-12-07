package com.netcraker.services;

import com.netcraker.model.Chat;

import java.util.List;

public interface ChatService {
    List<Chat> getContent(int user1_id, int user2_id);
    Chat createChat(int friendId, int userCurrentId);
}
