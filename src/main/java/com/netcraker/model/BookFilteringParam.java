package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public enum BookFilteringParam {
    TITLE(String.class), GENRE(Integer.class), AUTHOR(Integer.class), ANNOUNCEMENT_DATE(LocalDate.class);

    private final @NonNull Class clazz;
}
