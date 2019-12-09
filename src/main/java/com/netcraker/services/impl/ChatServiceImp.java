package com.netcraker.services.impl;

import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.FindException;
import com.netcraker.model.Chat;
import com.netcraker.model.Message;
import com.netcraker.model.User;
import com.netcraker.repositories.ChatRepository;
import com.netcraker.repositories.UserRepository;
import com.netcraker.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImp implements ChatService {

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;

    @Override
    public List<Message> getMessages(int friendId, int userCurrentId) {
        Optional<Chat> chatFromDB = chatRepository.findLocalChat(new int[]{friendId, userCurrentId});
        return generationMessages(chatFromDB);
    }

    @Override
    public List<Message> getGroupChatMessages(String chatName) {
        Optional<Chat> chatFromDB = chatRepository.findGroupChat(chatName);
        return generationMessages(chatFromDB);
    }

    private List<Message> generationMessages(Optional<Chat> chatFromDB) {
        int chatId = chatFromDB.get().getChatId();
        List<Message> messages = chatRepository.listMessage(chatId);
        messages.forEach(
                chatMessage -> {
                    Optional<User> userFromDB = userRepository.getById(chatMessage.getFromUser());
                    chatMessage.setFromUserName(userFromDB.get().getFullName());
                }
        );
        return messages;
    }

    @Override
    public List<Chat> getGroupChats(int userCurrentId) {
        List<Chat> chats = chatRepository.getGroupChatsReference(userCurrentId);
        for (Chat chat: chats) {
            Optional<Chat> tempDataChat =  chatRepository.findGroupChatById(chat.getGroupChatId());
            chat.setChatName(tempDataChat.get().getChatName());
        }
        return chats;
    }

    @Override
    public Chat createChat(int friendId, int userCurrentId) {
        Optional<Chat> chatFromDB = chatRepository.findLocalChat(new int[]{friendId, userCurrentId});
        if (chatFromDB.isPresent()) {
            throw new CreationException("Error in creating chat! Creation query failure");
        }
        if (friendId == 0) {
            throw new CreationException("Error in creating chat! Didn't select a friend");
        }
        int chatId = chatRepository.insert();
        return chatRepository.createLocalChat(new int[]{friendId, userCurrentId, chatId})
                .orElseThrow(() -> new CreationException("Error in creating chat! Creation query failure."));
    }

    @Override
    public Message sendMessage(Message message) {
        Optional<Chat> chatFromDB = chatRepository.findLocalChat(new int[]{message.getToUser(), message.getFromUser()});
        Optional<Message> sendingMessage = chatRepository.sendMessage(new int[]{message.getFromUser(), chatFromDB.get().getChatId()}, message.getContent());
        if (!sendingMessage.isPresent()) {
            throw new CreationException("Error in creating message!");
        }
        message.setChatId(chatFromDB.get().getChatId());
        this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.getChatId(), message);
        return message;
    }

    @Override
    public Message sendMessageGroupChat(Message message) {
        Optional<Chat> chatFromDB = chatRepository.findGroupChat(message.getChatName());
        Optional<Message> sendingMessage = chatRepository.sendMessage(new int[]{message.getFromUser(), chatFromDB.get().getChatId()}, message.getContent());
        if (!sendingMessage.isPresent()) {
            throw new CreationException("Error in creating message!");
        }
        this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.getChatName(), message);
        return message;
    }

    @Override
    public Chat getChat(int friendId, int userCurrentId) {
        return chatRepository.findLocalChat(new int[]{friendId, userCurrentId}).orElseThrow(() -> new FindException("Error in getting chat!"));
    }

    @Override
    public Chat createGroupChat(int[] usersId, String chatName) {
        if(chatName==null){
            throw  new CreationException("Error in creating chat! Didn't select a friend or names are equals");
        }
        Optional<Chat> chatFromDB = chatRepository.findGroupChat(chatName);
        if (chatFromDB.isPresent()) {
            return chatFromDB.orElseThrow(()-> new CreationException("Error in creating chat! Creation query failure"));
        }
        ///check
        if (usersId.length == 0 ) {
            throw new CreationException("Error in creating chat! Didn't select a friend or names are equals");
        }

        int chatId = chatRepository.insert();
        Chat newGroupChat = chatRepository.createGroupChat(chatName, chatId)
                .orElseThrow(() -> new CreationException("Error in creating group chat! Creation query failure."));

        for (int id: usersId) {
            Optional<Chat> groupChatReference = chatRepository.createGroupChatReference(id, newGroupChat.getGroupChatId());
            if(!groupChatReference.isPresent()) {
                throw new CreationException("Error in creating reference in chat! Didn't select a friend");
            }
        }
        return newGroupChat;
    }

}
