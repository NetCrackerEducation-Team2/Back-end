package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserAchievement {
    private int userAchievementId;
    private int userId;
    private int achievementId;
    LocalDateTime creationTime;
}
