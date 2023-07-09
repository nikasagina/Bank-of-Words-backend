package com.example.bankofwords.service;

import com.example.bankofwords.dao.*;
import com.example.bankofwords.parser.PdfParser;
import com.example.bankofwords.utils.JwtUtil;
import com.example.bankofwords.utils.WordUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UploadServiceTest {

    @InjectMocks
    private UploadService uploadService;

    @Mock
    private WordDAO wordDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private JwtUtil jwtUtil;

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
    void whenRequestsWithInvalidToken_returnsUnauthorizedResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(false);

        // Act
        ResponseEntity<?> response1 = uploadService.uploadWord(authHeader, 0L, "", "", null);
        ResponseEntity<?> response2 = uploadService.addImageToWord(authHeader, 0L, "", null);
        ResponseEntity<?> response3 = uploadService.uploadBook(authHeader, 0L, null);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response1.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response3.getStatusCode());
    }

    @Test
    void whenUploadWordRequestWithAlreadyAddedWord_returnsOkResponseWithSuccessfulFalse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";
        long userId = 1L;
        long tableId = 1L;

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(tableDAO.containsWord(tableId, word)).thenReturn(true);

        // Act
        ResponseEntity<?> response = uploadService.uploadWord(authHeader, tableId, word, "definition", null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertEquals(false, responseBody.get("successful"));
    }

    @Test
    void whenUploadWordRequestWithoutImage_returnsOkResponseWithSuccessfulTrue() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";
        long userId = 1L;
        long tableId = 1L;

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(tableDAO.containsWord(tableId, word)).thenReturn(false);

        // Act
        ResponseEntity<?> response = uploadService.uploadWord(authHeader, tableId, word, "definition", null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertEquals(true, responseBody.get("successful"));
    }

    @Test
    void whenUploadWordRequestWithImage_returnsOkResponseWithSuccessfulTrue() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";
        long userId = 1L;
        long tableId = 1L;

        byte[] imageData = {23, 12, 32};
        MultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                MimeTypeUtils.IMAGE_JPEG_VALUE,
                imageData
        );

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(tableDAO.containsWord(tableId, word)).thenReturn(false);

        // Mock the Files class and its write method
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.write(Mockito.any(), Mockito.any(byte[].class))).thenReturn(null);

            // Act
            ResponseEntity<?> response = uploadService.uploadWord(authHeader, tableId, word, "definition", image);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
            assert responseBody != null;
            assertEquals(true, responseBody.get("successful"));
        }
    }

    @Test
    void whenUploadWordRequestWithImageAndIOException_returnsErrorResponse() throws IOException {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";
        long userId = 1L;
        long tableId = 1L;
        MultipartFile image = Mockito.mock(MultipartFile.class);

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(tableDAO.containsWord(tableId, word)).thenReturn(false);
        when(image.getBytes()).thenThrow(IOException.class); // Throw IOException when attempting to read the bytes

        // Act
        ResponseEntity<?> response = uploadService.uploadWord(authHeader, tableId, word, "definition", image);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void whenAddImageToWordRequestSuccessful_returnsOkResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";
        long userId = 1L;
        long tableId = 1L;
        long wordId = 1L;

        byte[] imageData = {23, 12, 32};
        MultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                MimeTypeUtils.IMAGE_JPEG_VALUE,
                imageData
        );

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(wordDAO.getWordId(word, userId)).thenReturn(wordId);
        when(wordDAO.getWordCreator(wordId)).thenReturn(userId);

        // Mock the Files class and its write method
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.write(Mockito.any(), Mockito.any(byte[].class))).thenReturn(null);

            // Act
            ResponseEntity<?> response = uploadService.addImageToWord(authHeader, tableId, word, image);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void whenAddImageToWordRequestNonexistentWord_returnsBadRequestResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";
        long userId = 1L;
        long tableId = 1L;

        byte[] imageData = {23, 12, 32};
        MultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                MimeTypeUtils.IMAGE_JPEG_VALUE,
                imageData
        );

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(wordDAO.getWordId(word, userId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = uploadService.addImageToWord(authHeader, tableId, word, image);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void whenAddImageToWordRequestInitialWord_returnsBadRequestResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";
        long userId = 1L;
        long tableId = 1L;
        long wordId = 1L;

        byte[] imageData = {23, 12, 32};
        MultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                MimeTypeUtils.IMAGE_JPEG_VALUE,
                imageData
        );

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(wordDAO.getWordId(word, userId)).thenReturn(wordId);
        when(wordDAO.getWordCreator(wordId)).thenReturn(0L);

        // Act
        ResponseEntity<?> response = uploadService.addImageToWord(authHeader, tableId, word, image);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void whenAddImageToWordRequestWithoutImage_returnsBadRequestResponse() throws IOException {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";


        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);

        // Act
        ResponseEntity<?> response = uploadService.addImageToWord(authHeader, 1L, word,  null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void whenAddImageToWordRequestWithImageAndIOException_returnsInternalServerErrorResponse() throws IOException {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";
        long wordId = 1L;
        long userId = 1L;
        long tableId = 1L;
        MultipartFile image = Mockito.mock(MultipartFile.class);

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(wordDAO.getWordId(word, userId)).thenReturn(wordId);
        when(wordDAO.getWordCreator(wordId)).thenReturn(userId);
        when(image.getBytes()).thenThrow(IOException.class); // Throw IOException when attempting to read the bytes

        // Act
        ResponseEntity<?> response = uploadService.addImageToWord(authHeader, tableId, word, image);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void whenUploadBookRequestIsValid_returnsOkResponseWithData() throws IOException {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";
        String definition = "definition";
        long tableId = 1L;

        byte[] pdfContent = new byte[10];
        MockMultipartFile pdfFile = new MockMultipartFile("file", "book.pdf", null, pdfContent);

        List<String> unknownWords = new ArrayList<>(List.of(word));

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(pdfParser.parsePdf(pdfFile)).thenReturn(unknownWords);
        when(tableDAO.containsWord(tableId, word)).thenReturn(false);
        when(wordUtil.isSimple(word)).thenReturn(false);
        when(lexiconDAO.contains(word)).thenReturn(true);
        when(lexiconDAO.getWordDefinition(word)).thenReturn(definition);

        // Act
        ResponseEntity<?> response = uploadService.uploadBook(authHeader, tableId, pdfFile);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(true, responseBody.get("successful"));
        assertEquals(Map.of(word, definition), responseBody.get("added_words"));
    }

    @Test
    void whenUploadBookRequestInvalidFile_returnsBadRequestResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        byte[] pdfContent = new byte[10];
        MockMultipartFile pdfFile = new MockMultipartFile("file", "", null, pdfContent);

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);

        // Act
        ResponseEntity<?> response = uploadService.uploadBook(authHeader, 1L, pdfFile);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void whenUploadBookRequestWithIOException_returnsInternalServerErrorResponse() throws IOException {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        byte[] pdfContent = new byte[10];
        MockMultipartFile pdfFile = new MockMultipartFile("file", "book.pdf", null, pdfContent);

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(pdfParser.parsePdf(pdfFile)).thenThrow(IOException.class);

        // Act
        ResponseEntity<?> response = uploadService.uploadBook(authHeader, 1L, pdfFile);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
