package com.netcraker.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class Book {
    private int bookId;
    private String title;
    private int isbn;
    private LocalDate release;
    private int pages;
    private String filePath;
    private String photoPath;
    private String publishingHouse;
    private int rateSum = 0;
    private int votersCount = 0;
    private LocalDateTime creationTime;
    private @NonNull String slug;
    private @Singular List<Genre> genres;
    private @Singular List<Author> authors;
}
