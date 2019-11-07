package com.netcraker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class Genre {
    private int genreId;
    private String name;
    private String description;
}
