package com.netcraker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:jwt.properties")
public class SecurityKeyProvider {
    private final Environment environment;

    @Autowired
    public SecurityKeyProvider(Environment environment) {
        this.environment = environment;
    }

    public String getKey(){
        return environment.getProperty("jwt.secretKey");
    }
}
