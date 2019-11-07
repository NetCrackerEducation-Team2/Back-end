package com.netcraker.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Book {
    private int bookId;
    private @NonNull String title;
    private int isbn;
    private @NonNull LocalDate release;
    private int pages;
    private @NonNull String filePath;
    private String photoPath;
    private @NonNull String publishingHouse;
    private int rateSum;
    private int votersCount;
    private LocalDateTime creationTime;
    private @NonNull String slug;
}
