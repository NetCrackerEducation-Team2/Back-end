package com.netcraker.configuration;

import com.netcraker.controllers.ErrorHandlerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

    private static final int POOL_SIZE = 4;
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlerController.class);

    @Override
    public Executor getAsyncExecutor() {
        return Executors.newFixedThreadPool(POOL_SIZE);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable throwable, Method method, Object... params) -> {
            logger.error("Exception in async task: ");
            logger.error("Method name : {}", method.getName());
            logger.error("Exception message : {}", throwable.getMessage());
            for (Object param : params) {
                logger.error("Parameter value : {}", param);
            }
        };
    }
}
