package com.netcraker.services;

import com.netcraker.model.Chat;
import com.netcraker.model.Message;

import java.util.List;

public interface ChatService {
    Chat createChat(int friendId, int userCurrentId);
    Message sendMessage(Message message);
    List<Message> getMessages(int friendId, int userCurrentId);
    Chat getChat(int friendId, int userCurrentId);
}
