package com.example.bankofwords.service;

import com.example.bankofwords.dao.*;
import com.example.bankofwords.objects.Image;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.singletons.FlashcardAnswers;
import com.example.bankofwords.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private WordDAO wordDAO;
    @Mock
    private UserDAO userDAO;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private StatisticsDAO statisticsDAO;
    @Mock
    private ImageDAO imageDAO;
    @Mock
    private WordHistoryDAO wordHistoryDAO;


    private void setUpValidTokenMocks(String token, String username, long userId, boolean needsUserId) {
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(jwtUtil.validateToken(token, username)).thenReturn(true);
        if (needsUserId)
            when(userDAO.getUserID(username)).thenReturn(userId);
    }

    private void setUpValidTokenMocks(String token, String username, long userId) {
        setUpValidTokenMocks(token, username, userId, true);
    }

    @Test
    void whenStartRequestWithValidToken_returnsOkResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        long tableId = 1L;
        String username = "testUser";
        long userId = 1L;
        Word word = new Word(1L, "example", "definition", tableId);

        setUpValidTokenMocks("someToken", username, userId);
        when(wordDAO.getRandomWordFromTable(tableId)).thenReturn(word);

        // Act
        ResponseEntity<?> response = questionService.start(authHeader, tableId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertEquals(word.getDefinition(), responseBody.get("question"));
    }

    @Test
    void whenStartRequestWithInvalidToken_returnsUnauthorizedResponse() {
        // Arrange
        String authHeader = "Bearer invalidToken";
        Long tableId = 1L;
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("invalidToken")).thenReturn(username);
        when(jwtUtil.validateToken("invalidToken", username)).thenReturn(false);

        // Act
        ResponseEntity<?> response = questionService.start(authHeader, tableId);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void whenStartRequestWithNoMoreWordsInTable_returnsOkResponseWithErrorMessage() {
        // Arrange
        String authHeader = "Bearer someToken";
        long tableId = 1L;
        String username = "testUser";
        long userId = 1L;

        setUpValidTokenMocks("someToken", username, userId);
        when(wordDAO.getRandomWordFromTable(tableId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = questionService.start(authHeader, tableId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertEquals("No more words left to learn", responseBody.get("error"));
    }

    @Test
    void whenStartRequestWithNoMoreWordsWithoutTable_returnsOkResponseWithErrorMessage() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;

        setUpValidTokenMocks("someToken", username, userId);
        when(wordDAO.getRandomWordFromAll(userId)).thenReturn(null);
        // Act
        ResponseEntity<?> response = questionService.start(authHeader, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertEquals("No more words left to learn", responseBody.get("error"));
    }

    @Test
    void whenSpellingRequestWithValidToken_returnsOkResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        long tableId = 1L;
        String username = "testUser";
        long userId = 1L;
        Word word = new Word(1L, "example", "definition", tableId);

        setUpValidTokenMocks("someToken", username, userId);
        when(wordDAO.getRandomWordFromTable(tableId)).thenReturn(word);

        // Act
        ResponseEntity<?> response = questionService.spelling(authHeader, tableId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(word.getDefinition(), responseBody.get("question"));
    }

    @Test
    void whenSpellingRequestWithInvalidToken_returnsUnauthorizedResponse() {
        // Arrange
        String authHeader = "Bearer invalidToken";
        Long tableId = 1L;
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("invalidToken")).thenReturn(username);
        when(jwtUtil.validateToken("invalidToken", username)).thenReturn(false);

        // Act
        ResponseEntity<?> response = questionService.spelling(authHeader, tableId);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void whenSpellingRequestWithNoMoreWordsInTable_returnsOkResponseWithErrorMessage() {
        // Arrange
        String authHeader = "Bearer someToken";
        long tableId = 1L;
        String username = "testUser";
        long userId = 1L;

        setUpValidTokenMocks("someToken", username, userId);
        when(wordDAO.getRandomWordFromTable(tableId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = questionService.spelling(authHeader, tableId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("No more words left to learn", responseBody.get("error"));
    }

    @Test
    void whenSpellingRequestWithNoMoreWordsWithoutTable_returnsOkResponseWithErrorMessage() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;

        setUpValidTokenMocks("someToken", username, userId);
        when(wordDAO.getRandomWordFromAll(userId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = questionService.spelling(authHeader, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("No more words left to learn", responseBody.get("error"));
    }

    @Test
    void whenImageRequestWithValidToken_returnsOkResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        long tableId = 1L;
        String username = "testUser";
        long userId = 1L;
        Word word = new Word(1L, "example", "definition", tableId);
        Image image = new Image(1L, "imageUrl");

        setUpValidTokenMocks("someToken", username, userId, false);
        when(imageDAO.getRandomImageFromTable(userId)).thenReturn(image);
        when(wordDAO.getWordWithId(image.getWordId())).thenReturn(word);

        // Act
        ResponseEntity<?> response = questionService.image(authHeader, tableId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(image.getImageName(), responseBody.get("filename"));
    }

    @Test
    void whenImageRequestWithInvalidToken_returnsUnauthorizedResponse() {
        // Arrange
        String authHeader = "Bearer invalidToken";
        Long tableId = 1L;
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("invalidToken")).thenReturn(username);
        when(jwtUtil.validateToken("invalidToken", username)).thenReturn(false);

        // Act
        ResponseEntity<?> response = questionService.image(authHeader, tableId);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void whenImageRequestWithNoMoreWordsInTable_returnsOkResponseWithErrorMessage() {
        // Arrange
        String authHeader = "Bearer someToken";
        long tableId = 1L;
        String username = "testUser";
        long userId = 1L;

        setUpValidTokenMocks("someToken", username, userId, false);

        when(imageDAO.getRandomImageFromTable(tableId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = questionService.image(authHeader, tableId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("No images found", responseBody.get("error"));
    }

    @Test
    void whenImageRequestWithValidTokenWithoutTable_returnsOkResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        long tableId = 1L;
        String username = "testUser";
        long userId = 1L;
        Word word = new Word(1L, "example", "definition", tableId);
        Image image = new Image(1L, "imageUrl");

        setUpValidTokenMocks("someToken", username, userId);
        when(imageDAO.getRandomImage(userId)).thenReturn(image);
        when(wordDAO.getWordWithId(image.getWordId())).thenReturn(word);

        // Act
        ResponseEntity<?> response = questionService.image(authHeader, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(image.getImageName(), responseBody.get("filename"));
    }

    @Test
    void whenAnswerRequestWithInvalidToken_returnsUnauthorizedResponse() {
        // Arrange
        String authHeader = "Bearer invalidToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("invalidToken")).thenReturn(username);
        when(jwtUtil.validateToken("invalidToken", username)).thenReturn(false);

        // Act
        ResponseEntity<?> response = questionService.answer(authHeader, "", 0L);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void whenAnswerRequestWithValidTokenAndCorrectGuess_returnsOkResponseWithCorrectAnswer() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String guess = "correctGuess";
        long flashcardId = 1L;
        long tableId = 1L;
        long userId = 1L;
        long wordId = 2L;

        // Mock the dependencies and setup necessary data
        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(statisticsDAO.getTotalCount(userId, wordId)).thenReturn(3);
        when(statisticsDAO.getUserSuccessRateForWord(userId, wordId)).thenReturn(1.0);
        FlashcardAnswers flashcardAnswers = FlashcardAnswers.getInstance();
        Word correctAnswer = new Word(wordId, "correctGuess", "definition", tableId);
        flashcardAnswers.add(flashcardId, correctAnswer);


        // Act
        ResponseEntity<?> response = questionService.answer(authHeader, guess, flashcardId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("correct"));
        assertTrue(responseBody.containsKey("answer"));
        assertTrue(responseBody.containsKey("wordId"));
        assertTrue((Boolean) responseBody.get("correct"));
        assertEquals("correctGuess", responseBody.get("answer"));
        assertEquals(wordId, responseBody.get("wordId"));

        // Clean up
        flashcardAnswers.remove(flashcardId);
    }

    @Test
    void whenAnswerRequestWithValidTokenAndIncorrectGuess_returnsOkResponseWithCorrectAnswer() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String guess = "incorrectGuess";
        long flashcardId = 1L;
        long tableId = 1L;
        long userId = 1L;
        long wordId = 2L;

        // Mock the dependencies and setup necessary data
        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(statisticsDAO.getTotalCount(userId, wordId)).thenReturn(3);
        when(statisticsDAO.getUserSuccessRateForWord(userId, wordId)).thenReturn(1.0);
        FlashcardAnswers flashcardAnswers = FlashcardAnswers.getInstance();
        Word correctAnswer = new Word(wordId, "correctGuess", "definition", tableId);
        flashcardAnswers.add(flashcardId, correctAnswer);


        // Act
        ResponseEntity<?> response = questionService.answer(authHeader, guess, flashcardId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("correct"));
        assertTrue(responseBody.containsKey("answer"));
        assertTrue(responseBody.containsKey("wordId"));
        assertFalse((Boolean) responseBody.get("correct"));
        assertEquals("correctGuess", responseBody.get("answer"));
        assertEquals(wordId, responseBody.get("wordId"));

        // Clean up
        flashcardAnswers.remove(flashcardId);
    }

    @Test
    void whenAnswerRequestWithValidTokenAndInvalidFlashcardId_returnsNotFoundResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String guess = "guess";
        long flashcardId = 1L;
        long tableId = 1L;
        long wordId = 2L;

        // Mock the dependencies and setup necessary data
        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        FlashcardAnswers flashcardAnswers = FlashcardAnswers.getInstance();
        Word correctAnswer = new Word(wordId, "guess", "definition", tableId);
        flashcardAnswers.add(flashcardId, correctAnswer);

        // Act
        ResponseEntity<?> response = questionService.answer(authHeader, guess, -1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Clean up
        flashcardAnswers.remove(flashcardId);
    }

}