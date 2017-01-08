package com.promptu.utils;

/**
 * Created by Guy on 08/01/2017.
 */
public class BitwiseUtils {

    public static boolean AND(int target, int mask) {
        return (target & mask) != 0;
    }

}
