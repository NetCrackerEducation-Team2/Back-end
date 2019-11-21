package com.netcraker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableAsync
@Configuration
public class AsyncConfiguration {

    private final int MAIL_POOL_SIZE = 3;

    @Bean(name="mailThreadPoolTaskExecutor")
    public Executor mailThreadPoolTaskExecutor(){
        return Executors.newFixedThreadPool(MAIL_POOL_SIZE);
    }
}
