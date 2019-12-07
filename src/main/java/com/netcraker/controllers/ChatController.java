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
import java.util.Map;

@RestController
@RequestMapping({"/api/ws"})
@CrossOrigin(methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Message message){
        if(message.getToUser()!=null && !message.getToUser().equals("")){
            this.simpMessagingTemplate.convertAndSend("/socket-publisher/"+message.getToUser(),message);
            this.simpMessagingTemplate.convertAndSend("/socket-publisher/"+message.getFromUser(),message);
        }else {
            this.simpMessagingTemplate.convertAndSend("/socket-publisher",message);
        }
            return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<?> getMessages(@RequestParam int user1_id, @RequestParam int user2_id) {
//        List<Chat> messages  = chatService.getContent(user1_id, user2_id);
//        messages.forEach(
//                chatMessage -> {
//                    this.simpMessagingTemplate.convertAndSend("/socket-publisher",chatMessage.getContent());
//                }
//        );
//        return new ResponseEntity<>(messages, new HttpHeaders(), HttpStatus.OK);
//    }

    @PostMapping("/create")
    public ResponseEntity<?> createChat(@RequestBody @Validated Chat chat) {
        chatService.createChat(chat.getFriendId(), chat.getUserCurrentId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
