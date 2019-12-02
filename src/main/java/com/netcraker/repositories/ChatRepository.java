package com.netcraker.repositories;

import com.netcraker.model.ChatMessage;

import java.util.List;

public interface ChatRepository {
    List<ChatMessage> listMessage(int user1_id, int user2_id);
}
