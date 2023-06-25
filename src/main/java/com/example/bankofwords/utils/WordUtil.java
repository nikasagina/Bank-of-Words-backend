package com.example.bankofwords.utils;

import com.example.bankofwords.objects.Word;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
}
