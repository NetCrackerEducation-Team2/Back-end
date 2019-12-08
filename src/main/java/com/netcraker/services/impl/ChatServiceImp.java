package com.netcraker.services.impl;

import com.netcraker.exceptions.CreationException;
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
        int id = chatFromDB.get().getChatId();
        List<Message> messages = chatRepository.listMessage(id);
        messages.forEach(
                chatMessage -> {
                    Optional<User> userFromDB = userRepository.getById(chatMessage.getFromUser());
                    chatMessage.setFromUserName(userFromDB.get().getFullName());
                }
        );
        return messages;
    }

    @Override
    public Chat createChat(int friendId, int userCurrentId) {
        Optional<Chat> chatFromDB = chatRepository.findLocalChat(new int[]{friendId, userCurrentId});
        if (chatFromDB.isPresent()) {
            return chatFromDB.orElseThrow(() -> new CreationException("Error in creating chat! Creation query failure"));
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
        this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.getToUser(), message);
        this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.getFromUser(), message);
        return message;
    }

}
