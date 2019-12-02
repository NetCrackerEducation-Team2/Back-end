package com.netcraker.services.events.listeners;

import com.netcraker.model.Achievement;
import com.netcraker.model.constants.TableName;
import com.netcraker.services.AchievementService;
import com.netcraker.services.UserAchievementService;
import com.netcraker.services.UserService;
import com.netcraker.services.events.DataBaseChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DateBaseChangeEventListener {

    private final UserAchievementService userAchievementService;
    private final UserService userService;
    private final AchievementService achievementService;
    private final JdbcTemplate jdbcTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final static String destinationTopic = "/topic/achievements/";

    @Async
    @EventListener
    public void handleDataBaseChangeEvent(DataBaseChangeEvent event) {
        final TableName tableName = event.getAffectedTable();
        final int userId = event.getUserId().intValue();

        // Check all users in database
        if (userId < 0) {
            final List<Integer> allUserIds = userService.getListId();

            for (int uId : allUserIds) {
                final Optional<Achievement> optAchievement = checkAchievementCondition(tableName, uId);

                if (optAchievement.isPresent()) {
                    final Achievement achievement = optAchievement.get();
                    if (userAchievementService.addUserAchievement(uId, achievement.getAchievementId())) {
                        simpMessagingTemplate.convertAndSend(destinationTopic + uId, achievement.getAchievementId());
                    }
                }
            }
        } else {
            final Optional<Achievement> optAchievement = checkAchievementCondition(tableName, userId);

            if (optAchievement.isPresent()) {
                final Achievement achievement = optAchievement.get();
                if (userAchievementService.addUserAchievement(userId, achievement.getAchievementId())) {
                    simpMessagingTemplate.convertAndSend(destinationTopic + userId, achievement.getAchievementId());
                }
            }
        }
    }

    private Optional<Achievement> checkAchievementCondition(TableName tableName, int userId) {
        for (Achievement a : achievementService.getAchievementsByTableName(tableName)) {
            List<Boolean> result = jdbcTemplate.query(a.getSqlQuery(), (rs, i) -> rs.getBoolean(1), userId);

            boolean isComplete = result != null && !result.isEmpty() && result.get(0);

            if (isComplete && !userAchievementService.exists(userId, a.getAchievementId())) {
                return Optional.of(a);
            }
        }
        return Optional.empty();
    }
}
