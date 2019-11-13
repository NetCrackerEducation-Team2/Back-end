package com.netcraker.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class Book {
    private int bookId;
    private @NonNull String title;
    private int isbn;
    private LocalDate release;
    private int pages;
    private @NonNull String filePath;
    private String photoPath;
    private byte[] photo;
    private @NonNull String publishingHouse;
    private int rateSum;
    private int votersCount;
    private LocalDateTime creationTime;
    private @NonNull String slug;
    private @Singular List<Genre> genres;
    private @Singular List<Author> authors;
}
