package com.netcraker.model;

import lombok.*;

@Data @Builder
public class Author {
    private int authorId;
    private @NonNull String fullName;
    private String description;
}
