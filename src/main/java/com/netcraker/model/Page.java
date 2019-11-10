package com.netcraker.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Page<T> {
    private int currentPage;
    private int totalElements;
    private int pageElements;
    private int countPages;
    private ArrayList<T> array;
    public Page(int totalElements, int pageElements, int currentPage, ArrayList<T> array){
        this.totalElements = totalElements;
        this.pageElements = pageElements;
        this.countPages = Math.max(1, (int) Math.ceil((double) totalElements / pageElements));
        this.currentPage = Math.min(countPages, Math.max(1, currentPage));
        this.array = array;
    }
}
