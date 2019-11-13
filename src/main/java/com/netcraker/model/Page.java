package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor
public final class Page<T> {
    private final int currentPage;
    private final int countPages;
    private final List<T> array;
}
