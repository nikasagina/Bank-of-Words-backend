package com.example.bankofwords.service;

import com.example.bankofwords.dao.StatisticsDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.dao.WordHistoryDAO;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static java.lang.Double.NaN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    private JwtUtil jwtUtil;

    @Mock
    private StatisticsDAO statisticsDAO;

    @Mock
    private WordHistoryDAO wordHistoryDAO;


    @Test
    void whenStartRequestWithValidToken_returnsOkResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);

        // Act
        ResponseEntity<?> response1 = statisticsService.getUserStatistics(authHeader);
        ResponseEntity<?> response2 = statisticsService.getGlobalStatistics(authHeader);
        ResponseEntity<?> response3 = statisticsService.getUserActivity(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(HttpStatus.OK, response3.getStatusCode());
    }

    @Test
    void whenStartRequestWithInvalidToken_returnsUnauthorizedResponse() {
        // Arrange
        String authHeader = "Bearer invalidToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("invalidToken")).thenReturn(username);
        when(jwtUtil.validateToken("invalidToken", username)).thenReturn(false);

        // Act
        ResponseEntity<?> response1 = statisticsService.getUserStatistics(authHeader);
        ResponseEntity<?> response2 = statisticsService.getGlobalStatistics(authHeader);
        ResponseEntity<?> response3 = statisticsService.getUserActivity(authHeader);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response1.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response3.getStatusCode());
    }

    @Test
    void whenUserStatisticsRequestAndUserHasNotGuessed_returnsOkResponseWithEmptyValues() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(statisticsDAO.getUserTotalGuessesCount(userId)).thenReturn(0);
        when(statisticsDAO.getUserSuccessfulGuessesCount(userId)).thenReturn(0);
        when(statisticsDAO.getUserSuccessRateTotal(userId)).thenReturn(NaN);
        when(statisticsDAO.getMostGuessedWordByUser(userId)).thenReturn(0L);
        when(statisticsDAO.getLeastGuessedWordByUser(userId)).thenReturn(0L);
        when(wordDAO.getWordWithId(0)).thenReturn(null);

        // Act
        ResponseEntity<?> response = statisticsService.getUserStatistics(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertEquals(0L, responseBody.get("total_guesses_count"));
        assertEquals(0L, responseBody.get("success_guesses_count"));
        assertNull(responseBody.get("success_rate"));
        assertNull(responseBody.get("most_guessed_word"));
        assertNull(responseBody.get("least_guessed_word"));
    }

    @Test
    void whenUserStatisticsRequestAndOneUsersGuess_returnsOkResponseWithEmptyValues() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;
        Word word = new Word(1L, "example", "definition", 1L);

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(statisticsDAO.getUserTotalGuessesCount(userId)).thenReturn(1);
        when(statisticsDAO.getUserSuccessfulGuessesCount(userId)).thenReturn(1);
        when(statisticsDAO.getUserSuccessRateTotal(userId)).thenReturn(1.0);
        when(statisticsDAO.getMostGuessedWordByUser(userId)).thenReturn(word.getId());
        when(statisticsDAO.getLeastGuessedWordByUser(userId)).thenReturn(word.getId());
        when(wordDAO.getWordWithId(word.getId())).thenReturn(word);

        // Act
        ResponseEntity<?> response = statisticsService.getUserStatistics(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertEquals(1L, responseBody.get("total_guesses_count"));
        assertEquals(1L, responseBody.get("success_guesses_count"));
        assertEquals(1.0, responseBody.get("success_rate"));
        assertEquals(word.getWord(), responseBody.get("most_guessed_word"));
        assertEquals(word.getWord(), responseBody.get("least_guessed_word"));
    }


    @Test
    void whenUserActivityRequestAndUserHasNotGuessed_returnsOkResponseWithEmptyValues() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(wordHistoryDAO.getFullDailyActivity(userId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = statisticsService.getUserActivity(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertNull(responseBody.get("activity"));
    }

    @Test
    void whenGlobalStatisticsRequestAndNoOneHasGuessed_returnsOkResponseWithEmptyValues() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long defaultWordId = 0L;

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(statisticsDAO.getMostGuessedWordOverall()).thenReturn(defaultWordId);
        when(statisticsDAO.getLeastGuessedWordOverall()).thenReturn(defaultWordId);
        when(wordDAO.getWordWithId(defaultWordId)).thenReturn(null);
        when(statisticsDAO.getTopUserIdsWithBestSuccessRate(1)).thenReturn(List.of());
        when(statisticsDAO.getTopUserIdsWithBestSuccessRate(5)).thenReturn(List.of());

        // Act
        ResponseEntity<?> response = statisticsService.getGlobalStatistics(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertNull(responseBody.get("most_guessed_word"));
        assertNull(responseBody.get("least_guessed_word"));
        assertNull(responseBody.get("top_user"));
        assertEquals(List.of(), responseBody.get("top_5"));
    }


    @Test
    void whenGlobalStatisticsRequestWithOneUsersGuess_returnsOkResponseWithEmptyValues() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;
        Word word = new Word(1L, "example", "definition", 1L);

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(statisticsDAO.getMostGuessedWordOverall()).thenReturn(word.getId());
        when(statisticsDAO.getLeastGuessedWordOverall()).thenReturn(word.getId());
        when(wordDAO.getWordWithId(word.getId())).thenReturn(word);
        when(statisticsDAO.getTopUserIdsWithBestSuccessRate(1)).thenReturn(List.of(userId));
        when(statisticsDAO.getTopUserIdsWithBestSuccessRate(5)).thenReturn(List.of(userId));
        when(userDAO.getUsername(userId)).thenReturn(username);
        // Act
        ResponseEntity<?> response = statisticsService.getGlobalStatistics(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertEquals(word.getWord(), responseBody.get("most_guessed_word"));
        assertEquals(word.getWord(), responseBody.get("least_guessed_word"));
        assertEquals(username, responseBody.get("top_user"));
        assertEquals(List.of(username), responseBody.get("top_5"));
    }
}
