package com.example.bankofwords.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class DailyUserReport {
    private final long userId;
    private long totalAnswersNum;
    private long correctAnswersNum;

    public void addIncorrectAnswer() {
        totalAnswersNum += 1;
    }

    public void addCorrectAnswer() {
        correctAnswersNum += 1;
        totalAnswersNum += 1;
    }
}