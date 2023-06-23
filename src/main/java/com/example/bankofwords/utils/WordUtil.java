package com.example.bankofwords.utils;

import org.springframework.stereotype.Service;

@Service
public class WordUtil {
    public static boolean dictionaryContainsWord(String word) {
        return false;
    }

    public static String getDefinition(String word) {
        return word;
    }
}
