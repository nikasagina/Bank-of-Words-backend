package com.example.bankofwords.singletons;


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public final class FlashcardAnswers {
    private static FlashcardAnswers instance;

    private final Map<Long, String> answers;

    public FlashcardAnswers() {
        answers = new HashMap<>();
    }

    public static FlashcardAnswers getInstance() {
        if (instance == null){
            instance = new FlashcardAnswers();
        }

        return instance;
    }

    public void add(long id, String answer) {
        answers.put(id, answer);
    }

    public String getAnswer(long id) {
        return answers.get(id);
    }

    public void remove(long id) {
        answers.remove(id);
    }

    public boolean contains(long id) {
        return answers.containsKey(id);
    }
}
