package com.netcraker.controllers;

import com.netcraker.model.Chat;
import com.netcraker.model.Message;
import com.netcraker.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/ws"})
@CrossOrigin(methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Message message){
        chatService.sendMessage(message);
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getMessages(@RequestParam int friendId, @RequestParam int currentUserId) {
        List<Message> messages  = chatService.getMessages(friendId, currentUserId);
        return new ResponseEntity<>(messages, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createChat(@RequestBody @Validated Chat chat) {
        chatService.createChat(chat.getFriendId(), chat.getUserCurrentId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/getChat")
    public ResponseEntity<?> getChat(@RequestParam int friendId, @RequestParam int currentUserId) {
        Chat chat =  chatService.getChat(friendId, currentUserId);
        return new ResponseEntity<>(chat, new HttpHeaders(), HttpStatus.OK);
    }
}
