package com.netcraker.model;

import lombok.NonNull;

import java.time.LocalDateTime;

public class BookOverview {
    private int bookOverviewId;
    private @NonNull String description;
    private int bookId;
    private int userId;
    private boolean published;
    private LocalDateTime creationTime;
}
