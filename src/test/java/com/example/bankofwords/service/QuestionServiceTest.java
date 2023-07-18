package com.example.bankofwords.service;

import com.example.bankofwords.dao.ImageDAO;
import com.example.bankofwords.dao.StatisticsDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.dao.WordHistoryDAO;
import com.example.bankofwords.objects.Image;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.singletons.FlashcardAnswers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private WordDAO wordDAO;

    @Mock
    private StatisticsDAO statisticsDAO;

    @Mock
    private ImageDAO imageDAO;

    @Mock
    private WordHistoryDAO wordHistoryDAO;


    @Test
    void whenStartRequestWithValidToken_returnsOkResponse() {
        // Arrange
        long tableId = 1L;
        Word word = new Word(1L, "example", "definition", tableId);
        
        when(wordDAO.getRandomWordFromTable(tableId)).thenReturn(word);

        // Act
        Map<String, Object> response = questionService.start(tableId, 1);

        // Assert
        assertNotNull(response);
        assertEquals(word.getDefinition(), response.get("question"));
    }


    @Test
    void whenStartRequestWithNoMoreWordsInTable_returnsOkResponseWithErrorMessage() {
        // Arrange
        long tableId = 1L;

        when(wordDAO.getRandomWordFromTable(tableId)).thenReturn(null);

        // Act
        Map<String, Object> response = questionService.start(tableId, 0);
      
        assertNotNull(response);
        assertEquals("No more words left to learn", response.get("error"));
    }



    @Test
    void whenSpellingRequestWithValidToken_returnsOkResponse() {
        // Arrange
        long tableId = 1L;
        Word word = new Word(1L, "example", "definition", tableId);

        when(wordDAO.getRandomWordFromTable(tableId)).thenReturn(word);

        // Act
        Map<String, Object> response = questionService.spelling(tableId);

        // Assert
        assertEquals(word.getDefinition(), response.get("question"));
    }

    @Test
    void whenSpellingRequestWithNoMoreWordsInTable_returnsOkResponseWithErrorMessage() {
        // Arrange
        long tableId = 1L;

        when(wordDAO.getRandomWordFromTable(tableId)).thenReturn(null);

        // Act
        Map<String, Object> response = questionService.spelling(tableId);

        // Assert
        assertNotNull(response);
        assertEquals("No more words left to learn", response.get("error"));
    }


    @Test
    void whenImageRequestWithValidToken_returnsOkResponse() {
        // Arrange
        long tableId = 1L;
        long userId = 1L;
        Word word = new Word(1L, "example", "definition", tableId);
        Image image = new Image(1L, "imageUrl");


        when(imageDAO.getRandomImageFromTable(userId)).thenReturn(image);
        when(wordDAO.getWordWithId(image.getWordId())).thenReturn(word);

        // Act
        Map<String, Object> response = questionService.image(tableId);

        // Assert

      
        assertNotNull(response);
        assertEquals(image.getImageName(), response.get("filename"));
    }


    @Test
    void whenImageRequestWithNoMoreWordsInTable_returnsOkResponseWithErrorMessage() {
        // Arrange
        long tableId = 1L;

        when(imageDAO.getRandomImageFromTable(tableId)).thenReturn(null);

        // Act
        Map<String, Object> response = questionService.image(tableId);

        // Assert
        assertNotNull(response);
        assertEquals("No images found", response.get("error"));
    }



    @Test
    void whenAnswerRequestWithValidTokenAndCorrectGuess_returnsOkResponseWithCorrectAnswer() {
        // Arrange
        String guess = "correctGuess";
        long flashcardId = 1L;
        long tableId = 1L;
        long userId = 1L;
        long wordId = 2L;

        when(statisticsDAO.getTotalCount(userId, wordId)).thenReturn(3);
        when(statisticsDAO.getUserSuccessRateForWord(userId, wordId)).thenReturn(1.0);
        FlashcardAnswers flashcardAnswers = FlashcardAnswers.getInstance();
        Word correctAnswer = new Word(wordId, "correctGuess", "definition", tableId);
        flashcardAnswers.add(flashcardId, correctAnswer);

        // Act
        Map<String, Object> response = questionService.answer(userId, guess, flashcardId);

        // Assert
        assertNotNull(response);
        assertTrue(response.containsKey("correct"));
        assertTrue(response.containsKey("answer"));
        assertTrue(response.containsKey("wordId"));
        assertTrue((Boolean) response.get("correct"));
        assertEquals("correctGuess", response.get("answer"));
        assertEquals(wordId, response.get("wordId"));

        // Clean up
        flashcardAnswers.remove(flashcardId);

        // Reset the request attributes to avoid affecting other tests
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void whenAnswerRequestWithValidTokenAndIncorrectGuess_returnsOkResponseWithCorrectAnswer() {
        // Arrange
        String guess = "incorrectGuess";
        long flashcardId = 1L;
        long tableId = 1L;
        long userId = 1L;
        long wordId = 2L;

        when(statisticsDAO.getTotalCount(userId, wordId)).thenReturn(3);
        when(statisticsDAO.getUserSuccessRateForWord(userId, wordId)).thenReturn(1.0);
        FlashcardAnswers flashcardAnswers = FlashcardAnswers.getInstance();
        Word correctAnswer = new Word(wordId, "correctGuess", "definition", tableId);
        flashcardAnswers.add(flashcardId, correctAnswer);

        // Act
        Map<String, Object> response = questionService.answer(userId, guess, flashcardId);

        // Assert
        assertNotNull(response);
        assertTrue(response.containsKey("correct"));
        assertTrue(response.containsKey("answer"));
        assertTrue(response.containsKey("wordId"));
        assertFalse((Boolean) response.get("correct"));
        assertEquals("correctGuess", response.get("answer"));
        assertEquals(wordId, response.get("wordId"));

        // Clean up
        flashcardAnswers.remove(flashcardId);
    }

    @Test
    void whenAnswerRequestWithValidTokenAndInvalidFlashcardId_returnsNotFoundResponse() {
        // Arrange
        String guess = "guess";
        long flashcardId = 1L;
        long tableId = 1L;
        long wordId = 2L;

        FlashcardAnswers flashcardAnswers = FlashcardAnswers.getInstance();
        Word correctAnswer = new Word(wordId, "guess", "definition", tableId);
        flashcardAnswers.add(flashcardId, correctAnswer);

        // Act
        Map<String, Object> response = questionService.answer(1L, guess, -1);

        // Assert
        assertNull(response);

        // Clean up
        flashcardAnswers.remove(flashcardId);
    }

}