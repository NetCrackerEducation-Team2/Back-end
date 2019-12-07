package com.netcraker.repositories;

import com.netcraker.model.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    List<Chat> listMessage(int user1_id, int user2_id);
    int insert();
    Optional<Chat> createLocalChat(int[] param);
    Optional<Chat> findLocalChat(int chatId);
    Optional<Chat> findLocalChat(int[] users);
}
