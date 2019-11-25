package com.netcraker.services.events.listeners;

import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.FindException;
import com.netcraker.model.Achievement;
import com.netcraker.model.constants.TableName;
import com.netcraker.services.AchievementService;
import com.netcraker.services.UserAchievementService;
import com.netcraker.services.events.DataBaseChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DateBaseChangeEventListener {

    private final UserAchievementService userAchievementService;
    private final AchievementService achievementService;
    private final JdbcTemplate jdbcTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void handleCreationEvent(DataBaseChangeEvent event) {

        final TableName tableName = event.getAffectedTable();
        final int userId = event.getUserId().intValue();
//        System.out.println("DBCEListener: Changed in table : " + tableName + " userId: " + event.getUserId());
        achievementService.getAchievementsByTableName(tableName)
                .forEach(a -> {
                    // add checking if achievement was complete
                    if (Objects.requireNonNull(jdbcTemplate.queryForObject(a.getRequirement(), Boolean.class, userId))) {
                        if (userAchievementService.addUserAchievement(userId, a.getAchievementId())) {
                            simpMessagingTemplate
                                    .convertAndSend("/topic/achievements", a.getName());
                        }
                    }
                });
    }
}
