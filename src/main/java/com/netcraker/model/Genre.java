package com.netcraker.model;

import lombok.*;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    private int genreId;
    private @NonNull String name;
    private String description;
}
