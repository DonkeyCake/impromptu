package com.promptu.utils;

import java.util.Base64;
import java.util.Random;

/**
 * Created by Guy on 18/11/2016.
 */
public class StringGenerator {

    public static String generateString() {
        return Base64.getEncoder().encodeToString((new Random().nextInt()+"").getBytes());
    }

    public static String generateString(int length) {
        String s = generateString();
        if(s.length() > length)
            s = s.substring(length);
        return s;
    }

}
