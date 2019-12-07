package com.netcraker.services.impl;

import com.netcraker.exceptions.CreationException;
import com.netcraker.model.Chat;
import com.netcraker.repositories.ChatRepository;
import com.netcraker.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImp implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public List<Chat> getContent(int user1_id, int user2_id) {
        return chatRepository.listMessage(user1_id, user2_id);
    }

    @Override
    public Chat createChat(int friendId, int userCurrentId) {
        Optional<Chat> chatFromDB = chatRepository.findLocalChat(new int[]{ friendId, userCurrentId });
        if (chatFromDB.isPresent()) {
            return chatFromDB.orElseThrow(() -> new CreationException("Error in creating chat! Creation query failure"));
        }
        if(friendId == 0){
            throw new CreationException("Error in creating chat! Didn't select a friend");
        }
        int chatId = chatRepository.insert();
        return chatRepository.createLocalChat(new int[]{ friendId, userCurrentId, chatId })
                .orElseThrow(() -> new CreationException("Error in creating chat! Creation query failure."));
    }

}
