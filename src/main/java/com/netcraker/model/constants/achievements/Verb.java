package com.netcraker.model.constants.achievements;


public enum Verb {
    HAS("has"),
    READ("read"),
    WRITE("write");

    private String verb;

    Verb(String verb) {
        this.verb = verb;
    }

    public String getVerb() {
        return verb;
    }

}
