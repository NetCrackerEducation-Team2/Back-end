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

    @PostMapping("/group")
    public ResponseEntity<?> sendGroupMessage(@RequestBody Message message){
        chatService.sendMessageGroupChat(message);
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/getGroupChats")
    public ResponseEntity<?> getGroupChats(@RequestParam int userCurrentId){
        List<Chat> chats = chatService.getGroupChats(userCurrentId);
        return new ResponseEntity<>(chats, new HttpHeaders(), HttpStatus.OK);
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
    @GetMapping("/getGroupMessages")
    public ResponseEntity<?> getGroupChatMessages(@RequestParam String chatName) {
        List<Message> messages =  chatService.getGroupChatMessages(chatName);
        return new ResponseEntity<>(messages, new HttpHeaders(), HttpStatus.OK);
    }
    @PostMapping("/create/groupChat")
    public ResponseEntity<?> createGroupChat(@RequestBody @Validated Chat chat) {
        chatService.createGroupChat(chat.getUsersId(), chat.getChatName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
