package com.netcraker;

import com.netcraker.services.UserAchievementService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        final ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        final UserAchievementService bean = run.getBean(UserAchievementService.class);
        bean.addUserAchievement(19, 3);
    }
}