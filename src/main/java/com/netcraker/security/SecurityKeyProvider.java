package com.netcraker.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:jwt.properties")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityKeyProvider {
    private final @NonNull Environment environment;

    public String getKey(){
        return environment.getProperty("jwt.secretKey");
    }
}
