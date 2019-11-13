package com.netcraker;

import com.netcraker.repositories.BookReviewRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;


@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        final ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        final BookReviewRepository bean = run.getBean(BookReviewRepository.class);
    }
}