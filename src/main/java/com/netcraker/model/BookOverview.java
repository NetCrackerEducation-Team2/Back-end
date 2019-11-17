package com.netcraker.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookOverview {
    private int bookOverviewId;
    private @NonNull String description;
    private int bookId;
    private int userId;
    private boolean published;
    private LocalDateTime creationTime;
}
