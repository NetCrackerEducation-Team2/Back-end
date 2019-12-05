package com.netcraker.controllers;

import com.netcraker.model.ChatMessage;
import com.netcraker.services.impl.ChatServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/ws")
@CrossOrigin(methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatServiceImp chatServiceImp;

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> message){
        if(message.containsKey("message")){
            if(message.containsKey("toName") && message.get("toName")!=null && !message.get("toName").equals("")){

                this.simpMessagingTemplate.convertAndSend("/socket-publisher/"+message.get("toName"),message);
                this.simpMessagingTemplate.convertAndSend("/socket-publisher/"+message.get("fromName"),message);
            }else {

                this.simpMessagingTemplate.convertAndSend("/socket-publisher",message);
            }
            return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<?> getMessages(@RequestParam int user1_id, @RequestParam int user2_id) {
        List<ChatMessage> messages  = chatServiceImp.getContent(user1_id, user2_id);
        messages.forEach(
                chatMessage -> {
                    this.simpMessagingTemplate.convertAndSend("/socket-publisher",chatMessage.getContent());
                }
        );
        return new ResponseEntity<>(messages, new HttpHeaders(), HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<?> createChat


}
