package com.netcraker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String fromUser;
    private String toUser;
    private String content;
}
