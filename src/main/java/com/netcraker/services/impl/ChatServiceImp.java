package com.netcraker.services.impl;

import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.FindException;
import com.netcraker.model.Chat;
import com.netcraker.model.Message;
import com.netcraker.model.User;
import com.netcraker.model.constants.TableName;
import com.netcraker.repositories.ChatRepository;
import com.netcraker.repositories.UserRepository;
import com.netcraker.services.ChatService;
import com.netcraker.services.events.DataBaseChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImp implements ChatService {

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<Message> getMessages(int friendId, int userCurrentId) {
        Optional<Chat> chatFromDB = chatRepository.findLocalChat(new int[]{friendId, userCurrentId});
        return chatFromDB.map(this::generationMessages).orElseGet(ArrayList::new);
    }

    @Override
    public List<User> getUsers(String chatName) {
        return chatRepository.listChatUsers(chatName);
    }

    @Override
    public List<Message> getGroupChatMessages(String chatName) {
        Optional<Chat> chatFromDB = chatRepository.findGroupChat(chatName);
        return chatFromDB.map(this::generationMessages).orElseGet(ArrayList::new);
    }

    private List<Message> generationMessages(Chat chatFromDB) {
        int chatId = chatFromDB.getChatId();
        List<Message> messages = chatRepository.listMessage(chatId);
        messages.forEach(
                chatMessage -> {
                    Optional<User> userFromDB = userRepository.getById(chatMessage.getFromUser());
                    userFromDB.ifPresent(user -> chatMessage.setFromUserName(user.getFullName()));
                }
        );
        return messages;
    }

    @Override
    public List<Chat> getGroupChats(int userCurrentId) {
        List<Chat> chats = chatRepository.getGroupChatsReference(userCurrentId);
        for (Chat chat: chats) {
            Optional<Chat> tempDataChat =  chatRepository.findGroupChatById(chat.getGroupChatId());
            tempDataChat.ifPresent(value -> chat.setChatName(value.getChatName()));
        }
        return chats;
    }

    //no check
    @Override
    public Chat createChat(int friendId, int userCurrentId) {
        Optional<Chat> chatFromDB = chatRepository.findLocalChat(new int[]{friendId, userCurrentId});
        if (chatFromDB.isPresent()) {
            return chatFromDB.get();
        }
        int chatId = chatRepository.insert();
        return chatRepository.createLocalChat(new int[]{friendId, userCurrentId, chatId})
                .orElseThrow(() -> new CreationException("Error in creating chat! Creation query failure."));
    }

    @Override
    public Message sendMessage(Message message) {
        Optional<Chat> chatFromDB = chatRepository.findLocalChat(new int[]{message.getToUser(), message.getFromUser()});
        if (chatFromDB.isPresent()){
            Optional<Message> sendingMessage = chatRepository.sendMessage(new int[]{message.getFromUser(), chatFromDB.get().getChatId()}, message.getContent());
            if (!sendingMessage.isPresent()) {
                throw new CreationException("Error in creating message!");
            }
        }
        chatFromDB.ifPresent(chat -> message.setChatId(chat.getChatId()));
        this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.getChatId(), message);
        eventPublisher.publishEvent(new DataBaseChangeEvent<>(TableName.MESSAGES, message.getFromUser()));
        eventPublisher.publishEvent(new DataBaseChangeEvent<>(TableName.MESSAGES, message.getToUser()));
        return message;
    }

    @Override
    public Message sendMessageGroupChat(Message message) {
        Optional<Chat> chatFromDB = chatRepository.findGroupChat(message.getChatName());
        if(chatFromDB.isPresent()) {
            Optional<Message> sendingMessage = chatRepository.sendMessage(new int[]{message.getFromUser(), chatFromDB.get().getChatId()}, message.getContent());
            if (!sendingMessage.isPresent()) {
                throw new CreationException("Error in creating message!");
            }
        }
        this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.getChatName(), message);
        eventPublisher.publishEvent(new DataBaseChangeEvent<>(TableName.MESSAGES, message.getFromUser()));
        return message;
    }

    @Override
    public Chat getChat(int friendId, int userCurrentId) {
        return chatRepository.findLocalChat(new int[]{friendId, userCurrentId}).orElseThrow(() -> new FindException("Error in getting chat!"));
    }

    @Override
    public Chat createGroupChat(int[] usersId, String chatName) {
        Optional<Chat> chatFromDB = chatRepository.findGroupChat(chatName);
        if (chatFromDB.isPresent()) {
            return chatFromDB.orElseThrow(()-> new CreationException("Error in creating chat! Creation query failure"));
        }
        int chatId = chatRepository.insert();
        Optional<Chat> newGroupChat = chatRepository.createGroupChat(chatName, chatId);
        if (newGroupChat.isPresent()){
            for (int id: usersId) {
                chatRepository.createGroupChatReference(id, newGroupChat.get().getGroupChatId());
            }
        }
        return newGroupChat.orElseThrow(() -> new CreationException("Error in creating group chat"));
    }

    @Override
    public Chat addChatUser(int[] usersId, String chatName) {
        Optional<Chat> chatFromDB = chatRepository.findGroupChat(chatName);
        if (chatFromDB.isPresent()) {
            for (int id: usersId) {
                chatRepository.createGroupChatReference(id, chatFromDB.get().getGroupChatId());
            }
        }
        return chatFromDB.orElseThrow(()->new CreationException("Error in adding friends"));
    }

}
