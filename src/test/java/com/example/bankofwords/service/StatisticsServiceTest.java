package com.example.bankofwords.service;

import com.example.bankofwords.dao.StatisticsDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.dao.WordHistoryDAO;
import com.example.bankofwords.objects.DailyUserReport;
import com.example.bankofwords.objects.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.lang.Double.NaN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    @InjectMocks
    private StatisticsService statisticsService;

    @Mock
    private WordDAO wordDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private StatisticsDAO statisticsDAO;

    @Mock
    private WordHistoryDAO wordHistoryDAO;


    @Test
    public void testGetUserStatistics() {
        // Setup mock data
        long userId = 1L;
        when(statisticsDAO.getUserTotalGuessesCount(userId)).thenReturn(10);
        when(statisticsDAO.getUserSuccessfulGuessesCount(userId)).thenReturn(5);
        when(statisticsDAO.getUserSuccessRateTotal(userId)).thenReturn(0.5);
        when(statisticsDAO.getMostGuessedWordByUser(userId)).thenReturn(1L);
        when(statisticsDAO.getLeastGuessedWordByUser(userId)).thenReturn(2L);
        Word word1 = new Word(1L, "word1", "", 1L);
        Word word2 = new Word(1L, "word2", "", 1L);
        when(wordDAO.getWordWithId(1L)).thenReturn(word1);
        when(wordDAO.getWordWithId(2L)).thenReturn(word2);

        // Execute the service call
        Map<String, Object> userStatistics = statisticsService.getUserStatistics(userId);

        // Assertions
        assertEquals(10L, userStatistics.get("total_guesses_count"));
        assertEquals(5L, userStatistics.get("success_guesses_count"));
        assertEquals(0.5, userStatistics.get("success_rate"));
        assertEquals("word1", userStatistics.get("most_guessed_word"));
        assertEquals("word2", userStatistics.get("least_guessed_word"));
    }

    @Test
    public void testGetUserStatisticsWithHasNotGuessedUser() {
        // Setup mock data
        long userId = 1L;
        when(statisticsDAO.getUserTotalGuessesCount(userId)).thenReturn(0);
        when(statisticsDAO.getUserSuccessfulGuessesCount(userId)).thenReturn(0);
        when(statisticsDAO.getUserSuccessRateTotal(userId)).thenReturn(NaN);
        when(statisticsDAO.getMostGuessedWordByUser(userId)).thenReturn(1L);
        when(statisticsDAO.getLeastGuessedWordByUser(userId)).thenReturn(1L);

        when(wordDAO.getWordWithId(1L)).thenReturn(null);

        // Execute the service call
        Map<String, Object> userStatistics = statisticsService.getUserStatistics(userId);

        // Assertions
        assertEquals(0L, userStatistics.get("total_guesses_count"));
        assertEquals(0L, userStatistics.get("success_guesses_count"));
        assertNull(userStatistics.get("success_rate"));
        assertNull(userStatistics.get("most_guessed_word"));
        assertNull(userStatistics.get("least_guessed_word"));
    }


    @Test
    void whenUserActivityRequestAndUserHasNotGuessed_returnsOkResponseWithEmptyValues() {
        // Arrange
        long userId = 1L;
        Map<Date, DailyUserReport> activity = Map.of(Date.valueOf(LocalDate.now()), new DailyUserReport(userId));
        when(wordHistoryDAO.getFullDailyActivity(userId)).thenReturn(activity);

        // Act
        Map<String, Object> response = statisticsService.getUserActivity(userId);

        // Assert
        assertNotNull(response);
        assertEquals(activity, response.get("activity"));
    }

    @Test
    void whenGlobalStatisticsRequestAndNoOneHasGuessed_returnsOkResponseWithEmptyValues() {
        // Arrange
        long defaultWordId = 0L;

        when(statisticsDAO.getMostGuessedWordOverall()).thenReturn(defaultWordId);
        when(statisticsDAO.getLeastGuessedWordOverall()).thenReturn(defaultWordId);
        when(wordDAO.getWordWithId(defaultWordId)).thenReturn(null);
        when(statisticsDAO.getTopUserIdsWithBestSuccessRate(1)).thenReturn(List.of());
        when(statisticsDAO.getTopUserIdsWithBestSuccessRate(5)).thenReturn(List.of());

        // Act
        Map<String, Object> response = statisticsService.getGlobalStatistics();

        // Assert
        assertNotNull(response);
        assertNull(response.get("most_guessed_word"));
        assertNull(response.get("least_guessed_word"));
        assertNull(response.get("top_user"));
        assertEquals(List.of(), response.get("top_5"));
    }


    @Test
    void whenGlobalStatisticsRequestWithOneUsersGuess_returnsOkResponseWithEmptyValues() {
        // Arrange
        String username = "testUser";
        long userId = 1L;
        Word word = new Word(1L, "example", "definition", 1L);

        when(statisticsDAO.getMostGuessedWordOverall()).thenReturn(word.getId());
        when(statisticsDAO.getLeastGuessedWordOverall()).thenReturn(word.getId());
        when(wordDAO.getWordWithId(word.getId())).thenReturn(word);
        when(statisticsDAO.getTopUserIdsWithBestSuccessRate(1)).thenReturn(List.of(userId));
        when(statisticsDAO.getTopUserIdsWithBestSuccessRate(5)).thenReturn(List.of(userId));
        when(userDAO.getUsername(userId)).thenReturn(username);
        // Act
        Map<String, Object> response = statisticsService.getGlobalStatistics();

        // Assert
        assertNotNull(response);
        assertEquals(word.getWord(), response.get("most_guessed_word"));
        assertEquals(word.getWord(), response.get("least_guessed_word"));
        assertEquals(username, response.get("top_user"));
        assertEquals(List.of(username), response.get("top_5"));
    }
}
