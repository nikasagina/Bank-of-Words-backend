package com.example.bankofwords.objects;

import java.util.Date;

public class DailyUserReport {
    private final long userId;
    private long totalAnswersNum;
    private long correctAnswersNum;

    public DailyUserReport(long userId, Date date) {
        this.userId = userId;
        this.totalAnswersNum = 0;
        this.correctAnswersNum = 0;
    }

    public void addIncorrectAnswer() {
        totalAnswersNum += 1;
    }

    public void addCorrectAnswer() {
        correctAnswersNum += 1;
        totalAnswersNum += 1;
    }


    public long getUserId() {
        return userId;
    }

    public long getTotalAnswersNum() {
        return totalAnswersNum;
    }

    public long getCorrectAnswersNum() {
        return correctAnswersNum;
    }

    @Override
    public String toString() {
        return "DailyUserReport{" +
                "userId=" + userId +
                ", totalAnswersNum=" + totalAnswersNum +
                ", correctAnswersNum=" + correctAnswersNum +
                '}';
    }
}
