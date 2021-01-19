package com.popupmc.disenchantnpc.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TextUtils {
    // Convert words with spaces to words with the first letter in each word capitalized
    public static String convertToTitleCaseSplitting(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        return Arrays
                .stream(text.split(" "))
                .map(word -> word.isEmpty()
                        ? word
                        : Character.toTitleCase(word.charAt(0)) + word
                        .substring(1)
                        .toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
