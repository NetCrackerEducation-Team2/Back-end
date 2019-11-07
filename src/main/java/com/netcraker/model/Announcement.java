package com.netcraker.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Announcement {
    private int announcement_id;
    private String title;
    private String description;
    private int user_id;
    private boolean published;
    private LocalDateTime creation_time;
    private int book_id;
}
