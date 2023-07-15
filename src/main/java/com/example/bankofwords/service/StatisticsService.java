package com.example.bankofwords.service;

import com.example.bankofwords.dao.StatisticsDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.dao.WordHistoryDAO;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {
    private final WordDAO wordDAO;
    private final UserDAO userDAO;
    private final StatisticsDAO statisticsDAO;
    private final WordHistoryDAO wordHistoryDAO;

    @Autowired
    public StatisticsService(WordDAO wordDAO, UserDAO userDAO, StatisticsDAO statisticsDAO, WordHistoryDAO wordHistoryDAO) {
        this.wordDAO = wordDAO;
        this.userDAO = userDAO;
        this.statisticsDAO = statisticsDAO;
        this.wordHistoryDAO = wordHistoryDAO;
    }

    public Map<String, Object> getUserStatistics() {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        Map<String, Object> response = new HashMap<>();

        Long totalGuessesCount = (long) statisticsDAO.getUserTotalGuessesCount(userId);
        response.put("total_guesses_count", totalGuessesCount);

        Long successGuessesCount = (long) statisticsDAO.getUserSuccessfulGuessesCount(userId);
        response.put("success_guesses_count", successGuessesCount);

        Double successRate = statisticsDAO.getUserSuccessRateTotal(userId);
        if (!Double.isNaN(successRate)) {
            response.put("success_rate", successRate);
        } else {
            response.put("success_rate", null);
        }

        long mostGuessedWordId = statisticsDAO.getMostGuessedWordByUser(userId);
        Word mostGuessedWord = wordDAO.getWordWithId(mostGuessedWordId);
        if (mostGuessedWord != null) {
            response.put("most_guessed_word", mostGuessedWord.getWord());
        } else {
            response.put("most_guessed_word", null);
        }

        long leastGuessedWordId = statisticsDAO.getLeastGuessedWordByUser(userId);
        Word leastGuessedWord = wordDAO.getWordWithId(leastGuessedWordId);
        if (leastGuessedWord != null) {
            response.put("least_guessed_word", leastGuessedWord.getWord());
        } else {
            response.put("least_guessed_word", null);
        }

        return response;
    }

    public Map<String, Object> getGlobalStatistics() {
        Map<String, Object> response = new HashMap<>();

        long mostGuessedWordId = statisticsDAO.getMostGuessedWordOverall();
        Word mostGuessedWord = wordDAO.getWordWithId(mostGuessedWordId);
        if (mostGuessedWord != null) {
            response.put("most_guessed_word", mostGuessedWord.getWord());
        } else {
            response.put("most_guessed_word", null);
        }

        long leastGuessedWordId = statisticsDAO.getLeastGuessedWordOverall();
        Word leastGuessedWord = wordDAO.getWordWithId(leastGuessedWordId);
        if (leastGuessedWord != null) {
            response.put("least_guessed_word", leastGuessedWord.getWord());
        } else {
            response.put("least_guessed_word", null);
        }

        List<Long> topUserIds = statisticsDAO.getTopUserIdsWithBestSuccessRate(1);
        if (!topUserIds.isEmpty()) {
            Long topUserId = topUserIds.get(0);
            String topUsername = userDAO.getUsername(topUserId);
            if (topUsername != null) {
                response.put("top_user", topUsername);
            }
        } else {
            response.put("top_user", null);
        }

        List<Long> userIds = statisticsDAO.getTopUserIdsWithBestSuccessRate(5);
        List<String> topUsernames = new ArrayList<>();
        for (long userId : userIds) {
            String name = userDAO.getUsername(userId);
            if (name != null) {
                topUsernames.add(name);
            }
        }
        response.put("top_5", topUsernames);

        return response;
    }

    public Map<String, Object> getUserActivity() {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        return Map.of("activity", wordHistoryDAO.getFullDailyActivity(userId));
    }
}
