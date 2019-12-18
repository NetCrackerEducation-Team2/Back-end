package com.netcraker.model;

import lombok.*;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    private Integer genreId;
    private @NonNull String name;
    private String description;
}
