package com.example.bankofwords.service;

import com.example.bankofwords.dao.ImageDAO;
import com.example.bankofwords.dao.WordDAO;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlashcardServiceTest {

    @InjectMocks
    private FlashcardService flashcardService;

    @Mock
    private WordDAO wordDAO;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ImageDAO imageDAO;


    @Test
    void whenRequestsWithInvalidToken_returnsUnauthorizedResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(false);

        // Act
        ResponseEntity<?> response1 = flashcardService.getTextFront(authHeader, 1L);
        ResponseEntity<?> response2 = flashcardService.getImageFront(authHeader, 1L);
        ResponseEntity<?> response3 = flashcardService.getFlashcardBack(authHeader, 1L);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response1.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response3.getStatusCode());
    }

    @Test
    void whenGetTextFrontWithNoWordsInTable_returnsOkResponseWithErrorMessage() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long tableId = 1L;

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(wordDAO.getRandomWordFromTable(tableId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = flashcardService.getTextFront(authHeader, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("No more words left to learn", responseBody.get("error"));
    }

    @Test
    void whenValidGetTextFront_returnsOkResponseWithData() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long tableId = 1L;
        Word word = new Word(1L, "testWord", "definition", tableId);

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(wordDAO.getRandomWordFromTable(tableId)).thenReturn(word);

        // Act
        ResponseEntity<?> response = flashcardService.getTextFront(authHeader, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("id"));
        assertEquals(word.getDefinition(), responseBody.get("frontText"));
    }

    @Test
    void whenGetImageFrontWithNoImagesInTable_returnsOkResponseWithErrorMessage() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long tableId = 1L;

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(imageDAO.getRandomImageFromTable(tableId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = flashcardService.getImageFront(authHeader, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("No images found", responseBody.get("error"));
    }

    @Test
    void whenValidGetImageFront_returnsOkResponseWithData() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long tableId = 1L;
        Image image = new Image(1L, "testUrl");

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(imageDAO.getRandomImageFromTable(tableId)).thenReturn(image);

        // Act
        ResponseEntity<?> response = flashcardService.getImageFront(authHeader, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("id"));
        assertEquals(image.getImageName(), responseBody.get("imageUrl"));
    }

    @Test
    void whenGetFlashcardBackRequestWithNoneExistentId_returnsNotFoundResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);

        // Act
        ResponseEntity<?> response = flashcardService.getFlashcardBack(authHeader, -1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void whenAnswerRequestWithValidId_returnsOkResponseWithFlashcardData() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long flashcardId = 1L;
        long tableId = 1L;
        long wordId = 2L;

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);

        FlashcardAnswers flashcardAnswers = FlashcardAnswers.getInstance();
        Word correctAnswer = new Word(wordId, "correctGuess", "definition", tableId);
        flashcardAnswers.add(flashcardId, correctAnswer);

        // Act
        ResponseEntity<?> response = flashcardService.getFlashcardBack(authHeader, flashcardId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(correctAnswer.getWord(), responseBody.get("back"));

        // Clean up
        flashcardAnswers.remove(flashcardId);
    }
}
