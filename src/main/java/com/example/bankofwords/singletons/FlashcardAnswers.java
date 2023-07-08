package com.example.bankofwords.singletons;


import com.example.bankofwords.objects.Word;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public final class FlashcardAnswers {
    private static FlashcardAnswers instance;

    private final Map<Long, Word> answers;

    public FlashcardAnswers() {
        answers = new HashMap<>();
    }

    public static FlashcardAnswers getInstance() {
        if (instance == null){
            instance = new FlashcardAnswers();
        }

        return instance;
    }

    public void add(long flashcardId, Word answer) {
        answers.put(flashcardId, answer);
    }

    public Word getAnswer(long flashcardId) {
        return answers.get(flashcardId);
    }

    public void remove(long flashcardId) {
        answers.remove(flashcardId);
    }

    public boolean contains(long flashcardId) {
        return answers.containsKey(flashcardId);
    }
}
