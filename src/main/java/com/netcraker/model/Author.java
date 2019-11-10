package com.netcraker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class Author {
    private int authorId;
    private String fullName;
    private String description;
}
