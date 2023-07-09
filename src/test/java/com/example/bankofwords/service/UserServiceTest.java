package com.example.bankofwords.service;

import com.example.bankofwords.dao.TableDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Table;
import com.example.bankofwords.objects.User;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private WordDAO wordDAO;


    @Test
    void whenRequestsWithInvalidToken_returnsUnauthorizedResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(false);

        // Act
        ResponseEntity<?> response1 = userService.getInfo(authHeader);
        ResponseEntity<?> response2 = userService.getAllLearningWords(authHeader);
        ResponseEntity<?> response3 = userService.getAllLearnedWords(authHeader);

         // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response1.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response3.getStatusCode());
    }

    @Test
    void whenGetInfoRequest_returnsOkResponseInfo() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        User user = new User(username, "email@test.com", LocalDateTime.now());


        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserByUsername(username)).thenReturn(user);

        // Act
        ResponseEntity<?> response = userService.getInfo(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(user.getUsername(), responseBody.get("username"));
        assertEquals(user.getEmail(), responseBody.get("email"));
        assertEquals(user.getFormattedJoinDate(), responseBody.get("joinDate"));
    }

    @Test
    void whenGetAllLearningWordsRequest_returnsOkResponseWithWord() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;
        List<Word> wordList = List.of(new Word(1L, "testWord", "definition", 1L));

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(wordDAO.getAllLearningWords(userId)).thenReturn(wordList);

        // Act
        ResponseEntity<?> response = userService.getAllLearningWords(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(wordList, responseBody.get("learning_words"));
    }

    @Test
    void whenGetAllLearnedWordsRequest_returnsOkResponseWithWord() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;
        List<Word> wordList = List.of(new Word(1L, "testWord", "definition", 1L));

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(wordDAO.getAllLearnedWords(userId)).thenReturn(wordList);

        // Act
        ResponseEntity<?> response = userService.getAllLearnedWords(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(wordList, responseBody.get("learned_words"));
    }
}
