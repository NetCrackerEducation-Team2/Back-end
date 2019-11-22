package com.netcraker.services.impl;

import com.netcraker.services.PasswordGenerator;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class RandomPasswordGenerator implements PasswordGenerator {
    @Override
    public String generatePassword() {
        String[] symbols = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", " n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int indexRandom = random.nextInt(symbols.length);
            sb.append(symbols[indexRandom]);
        }
        return sb.toString();
    }
}
