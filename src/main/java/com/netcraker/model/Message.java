package com.netcraker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private int fromUser;
    private int toUser;
    private int chatId;
    private String content;
    private String fromUserName;
    private String chatName;

}
