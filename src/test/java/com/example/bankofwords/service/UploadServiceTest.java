package com.example.bankofwords.service;

import com.example.bankofwords.dao.*;
import com.example.bankofwords.parser.PdfParser;
import com.example.bankofwords.utils.WordUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UploadServiceTest {

    @InjectMocks
    private UploadService uploadService;

    @Mock
    private WordDAO wordDAO;

    @Mock
    private LexiconDAO lexiconDAO;

    @Mock
    private WordUtil wordUtil;

    @Mock
    private PdfParser pdfParser;

    @Mock
    private ImageDAO imageDAO;

    @Mock
    private TableDAO tableDAO;



    @Test
    public void testUploadWord() {
        String word = "test";
        String definition = "a procedure intended to establish the quality, performance, or reliability of something, especially before it is taken into widespread use.";
        long tableId = 1;
        long userId = 1;
        MockMultipartFile image = new MockMultipartFile("file", "filename.jpg", "image/jpg", "some-image".getBytes());

        when(tableDAO.containsWordAndDefinition(tableId, word, definition)).thenReturn(false);
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.write(Mockito.any(), Mockito.any(byte[].class))).thenReturn(null);

            uploadService.uploadWord(tableId, word, definition, image, userId);

            verify(wordDAO, times(1)).addWord(tableId, word, definition);
            verify(wordDAO, times(1)).getWordId(word, definition, userId);
            verify(imageDAO, times(1)).addImage(anyLong(), anyString());
        }
    }

    @Test
    public void testUploadAlreadyAddedWord() {
        String word = "test";
        String definition = "a procedure intended to establish the quality, performance, or reliability of something, especially before it is taken into widespread use.";
        long tableId = 1;
        long userId = 1;
        MockMultipartFile image = new MockMultipartFile("file", "filename.jpg", "image/jpg", "some-image".getBytes());

        when(tableDAO.containsWordAndDefinition(tableId, word, definition)).thenReturn(false);
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.write(Mockito.any(), Mockito.any(byte[].class))).thenThrow(new IOException());

            uploadService.uploadWord(tableId, word, definition, image, userId);

            verify(wordDAO, times(1)).addWord(tableId, word, definition);
            verify(wordDAO, times(1)).getWordId(word, definition, userId);
        }
    }

    @Test
    public void testUploadWordIOException() {
        String word = "test";
        String definition = "a procedure intended to establish the quality, performance, or reliability of something, especially before it is taken into widespread use.";
        long tableId = 1;
        long userId = 1;
        MockMultipartFile image = new MockMultipartFile("file", "filename.jpg", "image/jpg", "some-image".getBytes());

        when(tableDAO.containsWordAndDefinition(tableId, word, definition)).thenReturn(true);

        assertFalse(uploadService.uploadWord(tableId, word, definition, image, userId));
    }

    @Test
    public void testUploadBook() throws Exception {
        long tableId = 1;
        MockMultipartFile file = new MockMultipartFile("file", "filename.pdf", "application/pdf", "some-pdf".getBytes());

        List<String> unknownWords = Arrays.asList("test1", "test2");
        when(pdfParser.parsePdf(file)).thenReturn(unknownWords);

        when(tableDAO.containsWord(tableId, "test1")).thenReturn(false);
        when(tableDAO.containsWord(tableId, "test2")).thenReturn(false);
        when(wordUtil.isSimple("test1")).thenReturn(false);
        when(wordUtil.isSimple("test2")).thenReturn(false);
        when(lexiconDAO.contains("test1")).thenReturn(true);
        when(lexiconDAO.contains("test2")).thenReturn(true);

        when(lexiconDAO.getWordDefinition("test1")).thenReturn("definition1");
        when(lexiconDAO.getWordDefinition("test2")).thenReturn("definition2");

        uploadService.uploadBook(tableId, file);

        verify(wordDAO, times(1)).addWord(tableId, "test1", "definition1");
        verify(wordDAO, times(1)).addWord(tableId, "test2", "definition2");
    }

    @Test
    public void testUploadUnsupportedBook() {
        long tableId = 1;
        MockMultipartFile file = new MockMultipartFile("file", "filename.abc", "application/pdf", "some-pdf".getBytes());


        Map<String, Object> result = uploadService.uploadBook(tableId, file);

        assertTrue(result.containsKey("error"));
    }

    @Test
    public void testUploadBookIOException() throws Exception {
        long tableId = 1;
        MockMultipartFile file = new MockMultipartFile("file", "filename.pdf", "application/pdf", "some-pdf".getBytes());

        when(pdfParser.parsePdf(file)).thenThrow(new IOException());


        Map<String, Object> result = uploadService.uploadBook(tableId, file);

        assertTrue(result.containsKey("error"));
    }
}
