package com.netcraker.model.constants;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

public enum Parameter {

    // Common
    CREATED_TIME("created at"),

    // Book
    BOOK_VOTERS_COUNT(""),
    BOOK_RATE_SUM("rate sum"),
    BOOK_GENRE("genre"),
    BOOK_PAGES("book pages"),
    BOOK_RELEASE("book release"),
    BOOK_PUBLISHING_HOUSE("publishing house"),

    // Reserved book params
    RESERVED_BOOK_RATED("the most rated"),
    RESERVED_BOOK_NEWEST("the newest"),
    RESERVED_BOOK_OLDER("the older"),
    RESERVED_BOOK_LARGEST("the largest"),


    ;
    private final String parameter;

    Parameter(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
