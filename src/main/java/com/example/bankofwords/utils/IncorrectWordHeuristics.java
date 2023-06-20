package com.example.bankofwords.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class IncorrectWordHeuristics {

    private static final Random RANDOM = new Random();

    public static List<String> getIncorrectWords(String correct) {
        Set<String> result = new HashSet<>(); // Use a set to avoid duplicates

        while (result.size() < 3) {
            String misspelled = correct;

            // Apply a random mutation (swap, insert, delete, replace)
            int mutationType = RANDOM.nextInt(4);
            switch (mutationType) {
                case 0 -> misspelled = swapRandomAdjacentChars(misspelled);
                case 1 -> misspelled = insertRandomChar(misspelled);
                case 2 -> misspelled = deleteRandomChar(misspelled);
                case 3 -> misspelled = replaceRandomChar(misspelled);
            }

            if (!misspelled.equals(correct)) {
                result.add(misspelled);
            }
        }

        return new ArrayList<>(result);
    }

    private static String swapRandomAdjacentChars(String word) {
        int index = RANDOM.nextInt(word.length() - 1);
        return word.substring(0, index) +
                word.charAt(index + 1) +
                word.charAt(index) +
                word.substring(index + 2);
    }

    private static String insertRandomChar(String word) {
        int index = RANDOM.nextInt(word.length() + 1);
        char randomChar = (char) ('a' + RANDOM.nextInt(26));
        return word.substring(0, index) + randomChar + word.substring(index);
    }

    private static String deleteRandomChar(String word) {
        if (word.length() < 2) {
            return word;
        }
        int index = RANDOM.nextInt(word.length());
        return word.substring(0, index) + word.substring(index + 1);
    }

    private static String replaceRandomChar(String word) {
        int index = RANDOM.nextInt(word.length());
        char randomChar;
        do {
            randomChar = (char) ('a' + RANDOM.nextInt(26));
        } while (word.charAt(index) == randomChar);
        return word.substring(0, index) + randomChar + word.substring(index + 1);
    }
}