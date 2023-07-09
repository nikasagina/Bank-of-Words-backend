package com.example.bankofwords.service;

import com.example.bankofwords.dao.LexiconDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WordServiceTest {

    @InjectMocks
    private WordService wordService;

    @Mock
    private WordDAO wordDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private LexiconDAO lexiconDAO;


    @Test
    void whenRequestsWithInvalidToken_returnsUnauthorizedResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(false);

        // Act
        ResponseEntity<?> response1 = wordService.learn(authHeader, 0L);
        ResponseEntity<?> response2 = wordService.delete(authHeader, 0L);
        ResponseEntity<?> response3 = wordService.getDefinitions(authHeader, "");
        ResponseEntity<?> response4 = wordService.getWordInfo(authHeader, "");

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response1.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response3.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response4.getStatusCode());
    }

    @Test
    void whenLearnRequest_returnsOkResponseWithSuccess() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;
        long wordId = 1L;

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(wordDAO.alreadyKnows(userId, wordId)).thenReturn(false);

        // Act
        ResponseEntity<?> response = wordService.learn(authHeader, wordId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertEquals(true, responseBody.get("success"));
    }

    @Test
    void whenLearnRequest_returnsOkResponseWithSuccessFalse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;
        long wordId = 1L;

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(wordDAO.alreadyKnows(userId, wordId)).thenReturn(true);

        // Act
        ResponseEntity<?> response = wordService.learn(authHeader, wordId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertEquals(false, responseBody.get("success"));
    }

    @Test
    void whenDeleteRequest_returnsOkResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long wordId = 1L;

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);

        // Act
        ResponseEntity<?> response = wordService.delete(authHeader, wordId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void whenGetDefinitionsRequest_returnsOkResponseWithDefinitions() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";
        List<String> definitions = List.of("definition1", "definition2");

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(lexiconDAO.getDefinitions(word)).thenReturn(definitions);

        // Act
        ResponseEntity<?> response = wordService.getDefinitions(authHeader, word);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertEquals(definitions, responseBody.get("available_definitions"));
    }

    @Test
    void whenGetWordInfoRequestWithNoDefinitionsAvailable_returnsNoContent() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";
        String definition = "";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(lexiconDAO.getWordDefinition(word)).thenReturn(definition);

        // Act
        ResponseEntity<?> response = wordService.getWordInfo(authHeader, word);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void whenGetWordInfoRequest_returnsOkResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String word = "testWord";
        String definition = "definition1";
        List<String> definitions = new ArrayList<>(Arrays.asList("definition1", "definition2"));
        List<String> examples = List.of("example1", "example2");
        List<String> synonyms = List.of("synonym1", "synonym2");
        List<String> antonyms = List.of("antonym1", "antonym2");

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(lexiconDAO.getWordDefinition(word)).thenReturn(definition);
        when(lexiconDAO.getDefinitions(word)).thenReturn(definitions);
        when(lexiconDAO.getDefinitionExamples(word, definition)).thenReturn(examples);
        when(lexiconDAO.getWordSynonyms(word)).thenReturn(synonyms);
        when(lexiconDAO.getWordAntonyms(word)).thenReturn(antonyms);

        // Act
        ResponseEntity<?> response = wordService.getWordInfo(authHeader, word);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assert responseBody != null;
        assertEquals(word, responseBody.get("word"));
        assertEquals(definition, responseBody.get("definition"));
        assertEquals(List.of("definition2"), responseBody.get("other_definitions"));
        assertEquals(examples, responseBody.get("examples"));
        assertEquals(synonyms, responseBody.get("synonyms"));
        assertEquals(antonyms, responseBody.get("antonyms"));
    }
}
