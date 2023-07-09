package com.example.bankofwords.service;

import com.example.bankofwords.dao.TableDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Table;
import com.example.bankofwords.utils.JwtUtil;
import com.example.bankofwords.objects.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private TableDAO tableDAO;

    @Mock
    private WordDAO wordDAO;


    @Test
    void whenRequestsWithInvalidToken_returnsUnauthorizedResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        Table table = new Table(1L, 1L, "testTable");

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(false);

        // Act
        ResponseEntity<?> response1 = tableService.create(authHeader, table.getName());
        ResponseEntity<?> response2 = tableService.delete(authHeader, table.getTableId());
        ResponseEntity<?> response3 = tableService.initialTables(authHeader);
        ResponseEntity<?> response4 = tableService.userTables(authHeader);
        ResponseEntity<?> response5 = tableService.getWords(authHeader, table.getTableId());

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response1.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response3.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response4.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response5.getStatusCode());
    }

    @Test
    void whenCreateRequestsWithExistingTable_returnsOkResponseWithSuccessFalse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;

        Table table = new Table(1L, userId, "testTable");

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(tableDAO.existsTable(userId, table.getName())).thenReturn(true);

        // Act
        ResponseEntity<?> response = tableService.create(authHeader, table.getName());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(false, responseBody.get("success"));
    }

    @Test
    void whenCreateRequestsWithNewTable_returnsOkResponseWithSuccess() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;

        Table table = new Table(1L, userId, "testTable");

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(tableDAO.existsTable(userId, table.getName())).thenReturn(false);

        // Act
        ResponseEntity<?> response = tableService.create(authHeader, table.getName());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(true, responseBody.get("success"));
    }

    @Test
    void whenDeleteRequests_returnsOkResponse() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;

        Table table = new Table(1L, userId, "testTable");

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);

        // Act
        ResponseEntity<?> response = tableService.delete(authHeader, table.getTableId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void whenInitialTablesRequest_returnsOkResponseWithTable() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;

        Table table = new Table(1L, userId, "testTable");

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(tableDAO.getInitialTables()).thenReturn(List.of(table));

        // Act
        ResponseEntity<?> response = tableService.initialTables(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(List.of(table), responseBody.get("tables"));
    }

    @Test
    void whenUserTablesRequest_returnsOkResponseWithTable() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long userId = 1L;

        Table table = new Table(1L, userId, "testTable");

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(tableDAO.getUserTables(userId)).thenReturn(List.of(table));

        // Act
        ResponseEntity<?> response = tableService.userTables(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(List.of(table), responseBody.get("tables"));
    }

    @Test
    void whenGetWordsRequest_returnsOkResponseWithWords() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        long tableId = 1L;

        List<Word> wordList = List.of(new Word(1L, "testWord", "definition", tableId));

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(wordDAO.getTableWords(tableId)).thenReturn(wordList);

        // Act
        ResponseEntity<?> response = tableService.getWords(authHeader, tableId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(wordList, responseBody.get("words"));
    }
}
