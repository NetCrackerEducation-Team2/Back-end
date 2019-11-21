package com.netcraker.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    private int announcementId;
    private @NonNull String title;
    private @NonNull String description;
    private Integer userId;
    private boolean published;
    private LocalDateTime creationTime;
    private Integer bookId;
}
