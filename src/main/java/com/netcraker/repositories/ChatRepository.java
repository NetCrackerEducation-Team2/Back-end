package com.netcraker.repositories;

import com.netcraker.model.Chat;
import com.netcraker.model.Message;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    int insert();
    Optional<Chat> createLocalChat(int[] param);
    Optional<Chat> findLocalChat(int chatId);
    Optional<Chat> findLocalChat(int[] users);
    Optional<Message> sendMessage(int [] param, String content);
    Optional<Message> findMessage(int message_id);
    List<Message> listMessage(int chat_id);
}
