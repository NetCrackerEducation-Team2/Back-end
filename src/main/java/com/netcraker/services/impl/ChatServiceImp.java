package com.netcraker.services.impl;

import com.netcraker.model.ChatMessage;
import com.netcraker.repositories.ChatRepository;
import com.netcraker.repositories.impl.ChatRepositoryImpl;
import com.netcraker.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImp implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public List<ChatMessage> getContent(int user1_id, int user2_id) {
        return chatRepository.listMessage(user1_id, user2_id);
    }

    @Override
    public ChatMessage createMessage() {
        return null;
    }
}
