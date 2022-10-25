package com.adonis.haichanbank.utils;

import java.util.Random;

public class RandomString {
    public static String make(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int len = length;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public static String makeNumberString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int len = length;
        Random gen = new Random();
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int number = gen.nextInt(9 - 1 + 1) + 1;
            value.append(number);
        }
        return value.toString();
    }
}
