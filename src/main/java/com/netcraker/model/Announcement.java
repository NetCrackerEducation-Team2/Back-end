package com.netcraker.model;

import com.netcraker.model.annotations.EntityId;
import com.netcraker.model.annotations.GenericModel;
import com.netcraker.model.mapper.AnnouncementRowMapper;
import lombok.*;

import javax.swing.tree.RowMapper;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@GenericModel("announcements")
public class Announcement implements Entity {
    @EntityId("announcement_id")
    private int announcementId;
    private @NonNull String title;
    private @NonNull String description;
    private Integer userId;
    private boolean published;
    private LocalDateTime creationTime;
    private Integer bookId;

    @Override
    public Integer getId() {
        return getAnnouncementId();
    }
}
