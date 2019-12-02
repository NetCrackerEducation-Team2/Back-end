package com.netcraker.services;

import com.netcraker.model.ChatMessage;

import java.util.List;

public interface ChatService {
    List<ChatMessage> getContent(int user1_id, int user2_id);
    ChatMessage createMessage();
}
