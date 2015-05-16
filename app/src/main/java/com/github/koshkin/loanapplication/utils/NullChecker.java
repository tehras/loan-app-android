package com.github.koshkin.loanapplication.utils;

/**
 * Created by tehras on 5/16/15.
 */
public class NullChecker {

    public static boolean isNullOrEmpty(String string) {
        if (string == null || string.equalsIgnoreCase(""))
            return true;

        return false;
    }
}
