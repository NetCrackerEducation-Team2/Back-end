package com.netcraker.model;

import lombok.*;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private int authorId;
    private @NonNull String fullName;
    private String description;
}
