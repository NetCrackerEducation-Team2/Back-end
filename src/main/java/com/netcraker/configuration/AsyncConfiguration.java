package com.netcraker.configuration;

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
public class AsyncConfiguration implements AsyncConfigurer{

    private static final int POOL_SIZE = 4;

    @Override
    public Executor getAsyncExecutor() {
        return Executors.newFixedThreadPool(POOL_SIZE);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable throwable, Method method, Object... params) -> {
//            System.err.println("Exception in async : ");
//            System.err.println("Method name : " + method.getName());
//            System.err.println("Exception message : " + throwable.getMessage());
//            for (Object param : params) {
//                System.err.println("Parameter value : " + param);
//            }
            throwable.printStackTrace();
        };
    }

}
