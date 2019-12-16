package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    private Integer activityId;
    private String name;
    private String description;
    private Integer userId;
    private Timestamp creationTime;

    public static class ActivityBuilder {
        public ActivityBuilder announcementActivity(Announcement announcement, User user) {
            return this.name("CREATE_ANNOUNCEMENT")
                    .description(user.getFullName() + " created new announcement \"" + announcement.getTitle() + "\"")
                    .userId(user.getUserId())
                    .creationTime(new Timestamp(System.currentTimeMillis()));
        }

        public ActivityBuilder addBookReviewActivity(BookReview bookReview, Book book, User user) {
            return this.name("CREATE_REVIEW")
                    .description(user.getFullName() + " created new book review for book " + book.getTitle())
                    .userId(user.getUserId())
                    .creationTime(new Timestamp(System.currentTimeMillis()));
        }
    }
}
