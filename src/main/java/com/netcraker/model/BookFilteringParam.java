package com.netcraker.model;

import java.time.LocalDate;

public enum BookFilteringParam {
    TITLE(String.class), GENRE(Integer.class), AUTHOR(Integer.class), ANNOUNCEMENT_DATE(LocalDate.class);

    private final Class clazz;

    BookFilteringParam(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }
}
