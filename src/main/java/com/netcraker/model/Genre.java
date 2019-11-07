package com.netcraker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Genre {
    private int genreId;
    private @NonNull String name;
    private String description;
}
