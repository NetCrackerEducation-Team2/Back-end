package com.netcraker.services.impl;

import com.netcraker.services.PasswordGenerator;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
public class RandomPasswordGenerator implements PasswordGenerator {
    @Override
    public String generatePassword() throws NoSuchAlgorithmException {
        long start = System.currentTimeMillis();
        String[] symbols = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", " n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "_"};
        Random random = SecureRandom.getInstanceStrong();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int indexRandom = random.nextInt(symbols.length);
            sb.append(symbols[indexRandom]);
        }
        System.out.println("Took time to generate password: " + (System.currentTimeMillis() - start));
        return sb.toString();
    }
}
