package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public enum UserBookFilteringParam {
    USER_ID(Integer.class), TITLE(String.class), GENRE(Integer.class), AUTHOR(Integer.class), ANNOUNCEMENT_DATE(LocalDate.class),
    READ_MARK(Boolean.class), DONT_SEARCH_BY_READ_MARK(Boolean.class),
    FAVORITE_MARK(Boolean.class), DONT_SEARCH_BY_FAVORITE_MARK(Boolean.class);

    private final @NonNull Class clazz;
}
