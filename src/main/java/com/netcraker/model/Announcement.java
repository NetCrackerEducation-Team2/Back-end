package com.netcraker.model;

import com.netcraker.model.annotations.EntityId;
import com.netcraker.model.annotations.GenericModel;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@GenericModel("announcements")
public class Announcement {
    @EntityId("announcement_id")
    private int announcementId;
    private @NonNull String title;
    private @NonNull String description;
    private Integer userId;
    private boolean published;
    private LocalDateTime creationTime;
    private Integer bookId;
}
