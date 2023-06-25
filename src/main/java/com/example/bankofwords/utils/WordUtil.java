package com.example.bankofwords.utils;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WordUtil {
    private static final String[] simpleWords = {
            "the", "and", "is", "are", "it", "in", "on", "at", "to", "for",
            "from", "with", "that", "this", "but", "or", "not", "a", "an",
            "i", "you", "he", "she", "we", "they", "of", "by", "as", "at",
            "on", "in", "with", "up", "down", "out", "first", "second",
            "third", "fourth", "fifth", "sixth", "seventh", "eighth",
            "ninth", "tenth", "one", "two", "three", "four", "five",
            "six", "seven", "eight", "nine", "ten", "zero", "1st", "2nd",
            "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th", "yes",
            "no", "all", "any", "be", "do", "does", "did", "has", "had",
            "have", "if", "my", "your", "his", "her", "its", "it's", "our",
            "their", "so", "too", "very", "than", "just", "only", "here",
            "there", "when", "where", "why", "how", "what", "which", "who",
            "whom", "whose", "until", "while", "since", "ago"
    };
    private final Set<String> simpleWordsSet;

    public WordUtil() {
        simpleWordsSet = new HashSet<>();
        Collections.addAll(simpleWordsSet, simpleWords);
    }

    public boolean isSimple(String word) {
        return isNumber(word) || simpleWordsSet.contains(word.toLowerCase()) || word.length() < 3;
    }

    private boolean isNumber(String string) {
        String pattern = "^-?\\d+(\\.\\d+)?$";
        return Pattern.matches(pattern, string);
    }

    public static List<String> extractStringsInQuotes(String input) {
        List<String> resultList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            resultList.add(matcher.group(1));
        }

        return resultList;
    }
}
