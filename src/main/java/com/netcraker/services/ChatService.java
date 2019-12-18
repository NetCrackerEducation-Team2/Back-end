package com.netcraker.services;

import com.netcraker.model.Chat;
import com.netcraker.model.Message;
import com.netcraker.model.User;

import java.util.List;

public interface ChatService {
    Chat createChat(int friendId, int userCurrentId);
    Message sendMessage(Message message);
    List<Message> getMessages(int friendId, int userCurrentId);
    Chat getChat(int friendId, int userCurrentId);
    Chat createGroupChat(int[] usersId, String chatName);
    Message sendMessageGroupChat(Message message);
    List<Message> getGroupChatMessages(String chatName);
    List<Chat> getGroupChats(int userCurrentId);
    List<User> getUsers(String chatName);
    Chat addChatUser(int[] usersId, String chatName);
}
