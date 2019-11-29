package com.netcraker.services.events;

import org.springframework.context.ApplicationEvent;

public abstract class CustomEvent extends ApplicationEvent {
    public CustomEvent(Object source){
        super(source);
    }
    protected CustomEvent(){super(new Object());}
}
