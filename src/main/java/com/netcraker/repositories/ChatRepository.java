package com.netcraker.repositories;

import com.netcraker.model.Chat;
import com.netcraker.model.Message;
import com.netcraker.model.User;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    int insert();
    List<User> listChatUsers(String chatName);
    Optional<Chat> createLocalChat(int[] param);
    Optional<Chat> createGroupChat(String chatName, int chatId);
    Optional<Chat> findLocalChat(int chatId);
    Optional<Chat> findLocalChat(int[] users);
    Optional<Message> sendMessage(int [] param, String content);
    Optional<Message> findMessage(int message_id);
    List<Message> listMessage(int chat_id);
    Optional<Chat> findGroupChat(String chatName);
    Optional<Chat> createGroupChatReference(int userId, int chatId);
    Optional<Chat>findReferenceGroupChat(int groupChatUsersId);
    List<Chat> getGroupChatsReference(int userCurrentId);
    Optional<Chat> findGroupChatById(int groupChatId);
}
