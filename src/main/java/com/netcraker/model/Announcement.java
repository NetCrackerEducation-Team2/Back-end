package com.netcraker.model;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class Announcement {
    private @NonNull int announcement_id;
    private @NonNull String title;
    private @NonNull String description;
    private @NonNull int user_id;
    private boolean published;
    private LocalDateTime creation_time;
    private @NonNull int book_id;
}
