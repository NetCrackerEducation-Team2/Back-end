package com.netcraker.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private int bookId;
    private @NonNull String title;
    private long isbn;
    private LocalDate release;
    private int pages;
    private String filePath;
    private String photoPath;
    private @NonNull String publishingHouse;
    private int rateSum;
    private int votersCount;
    private LocalDateTime creationTime;
    private @NonNull String slug;
    private @Singular List<Genre> genres;
    private @Singular List<Author> authors;
}
