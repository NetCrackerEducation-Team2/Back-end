package com.netcraker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter @Setter @Builder
public class Author {
    private int authorId;
    private @NonNull String fullName;
    private String description;
}
