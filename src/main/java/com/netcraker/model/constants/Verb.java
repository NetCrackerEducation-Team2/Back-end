package com.netcraker.model.constants;


public enum Verb {
    HAS("has"),
    READ("read"),
    PUBLISH("publish");

    private String verb;

    Verb(String verb) {
        this.verb = verb;
    }

    public String getVerb() {
        return verb;
    }

}
