package com.example.bankofwords.service;

import com.example.bankofwords.dao.ImageDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Image;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.singletons.FlashcardAnswers;
import com.example.bankofwords.singletons.UniqueIdGenerator;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlashcardServiceTest {

    @InjectMocks
    private FlashcardService flashcardService;

    @Mock
    private WordDAO wordDAO;

    @Mock
    private ImageDAO imageDAO;


    @Test
    void testGetTextFront() {
        Word word = new Word(1L, "word", "definition", 1L);
        when(wordDAO.getRandomWordFromTable(anyLong())).thenReturn(word);

        Map<String, Object> response = flashcardService.getTextFront(1L);

        assertNotNull(response);
        assertEquals("definition", response.get("frontText"));
    }

    @Test
    void testGetTextFrontNoWord() {
        when(wordDAO.getRandomWordFromTable(anyLong())).thenReturn(null);

        Map<String, Object> response = flashcardService.getTextFront(1L);

        assertNotNull(response);
        assertEquals("No more words left to learn", response.get("error"));
    }

    @Test
    void testGetImageFront() {
        Image image = new Image(1L, "imageName");
        Word word = new Word(1L, "word", "definition", 1L);
        when(imageDAO.getRandomImageFromTable(anyLong())).thenReturn(image);
        when(wordDAO.getWordWithId(anyLong())).thenReturn(word);

        Map<String, Object> response = flashcardService.getImageFront(1L);

        assertNotNull(response);
        assertEquals("imageName", response.get("imageUrl"));
    }

    @Test
    void testGetImageFrontNoImage() {
        when(imageDAO.getRandomImageFromTable(anyLong())).thenReturn(null);

        Map<String, Object> response = flashcardService.getImageFront(1L);

        assertNotNull(response);
        assertEquals("No images found", response.get("error"));
    }

    @Test
    void testGetFlashcardBack() {
        Word word = new Word(1L, "word", "definition", 1L);
        FlashcardAnswers flashcardAnswers = FlashcardAnswers.getInstance();
        flashcardAnswers.add(1L, word);

        String result = flashcardService.getFlashcardBack(1L);

        assertNotNull(result);
        assertEquals("word", result);
    }

    @Test
    void testGetFlashcardBackNoFlashcard() {
        String result = flashcardService.getFlashcardBack(-1L);

        assertNull(result);
    }
}
