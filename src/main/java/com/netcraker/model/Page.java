package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter @AllArgsConstructor
public class Page<T> {
    private int currentPage;
    private int countPages;
    private ArrayList<T> array;
}
