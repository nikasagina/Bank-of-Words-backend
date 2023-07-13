package com.example.bankofwords.service;
import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.example.bankofwords.dao.*;
import com.example.bankofwords.objects.Table;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.utils.JwtUtil;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;

    @Mock
    private WordDAO wordDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ImageDAO imageDAO;

    @Mock
    private TableDAO tableDAO;


    @Test
    void whenRequestsWithInvalidToken_returnsUnauthorizedResponse() throws IOException {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(false);

        // Act
        ResponseEntity<?> response1 = transferService.importTable(authHeader, null);
        ResponseEntity<?> response2 = transferService.exportTable(authHeader, null);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response1.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());
    }

    @Test
    void whenImportingTableWithExistingName_returnsErrorResponse() throws IOException {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String tableName = "existingTableName";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(1L);
        when(tableDAO.getUserTables(1L)).thenReturn(List.of(new Table(1L, 1L, tableName)));

        Map<String, Object> json = new HashMap<>();
        json.put("tableName", tableName);
        json.put("words", List.of(Map.of("word", "foo", "definition", "bar", "imageUrl", "test.json"),
                                  Map.of("word", "bar", "definition", "baz", "imageUrl", "")));

        MockMultipartFile file = new MockMultipartFile("file", "test.json", MediaType.APPLICATION_JSON_VALUE,
                new ObjectMapper().writeValueAsString(json).getBytes());

        // Act
        ResponseEntity<?> response = transferService.importTable(authHeader, file);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertTrue(responseBody.containsKey("error"));
        assertEquals("You already have a table with the same name as the imported table", responseBody.get("error"));
    }

    @Test
    void whenImportingTableWithValidToken_returnsSuccessResponse() throws IOException {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String tableName = "newTableName";
        long userId = 1L;
        Table table = new Table(1L, 1L, tableName);

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(userId);
        when(tableDAO.createTable(userId, tableName)).thenReturn(table);
        when(tableDAO.getUserTables(userId)).thenReturn(List.of());

        Map<String, Object> json = new HashMap<>();
        json.put("tableName", tableName);
        json.put("words", List.of(Map.of("word", "foo", "definition", "bar", "imageUrl", "test.json"),
                Map.of("word", "bar", "definition", "baz", "imageUrl", "")));

        MockMultipartFile file = new MockMultipartFile("file", "test.json", MediaType.APPLICATION_JSON_VALUE,
                new ObjectMapper().writeValueAsString(json).getBytes());

        // Act
        ResponseEntity<?> response = transferService.importTable(authHeader, file);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertTrue(responseBody.containsKey("message"));
        assertEquals("Table imported successfully.", responseBody.get("message"));
    }

    @Test
    void whenExportingTableWithValidTokenAndTableName_returnsFileResource() throws IOException {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";
        String tableName = "existingTableName";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);
        when(userDAO.getUserID(username)).thenReturn(1L);
        when(tableDAO.getTableId(1L, tableName)).thenReturn(1L);
        when(tableDAO.getTable(1L)).thenReturn(new Table(1L, 1L, tableName));
        when(wordDAO.getTableWords(1L)).thenReturn(List.of(new Word(1L, "bar", "foo", 1L)));
        when(imageDAO.getImageUrl(1L)).thenReturn("");
        // Act
        ResponseEntity<Resource> response = transferService.exportTable(authHeader, tableName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Resource responseBody = response.getBody();
        assertNotNull(responseBody.getFile());
        assertEquals(tableName + ".json", responseBody.getFilename());

        // Cleanup
        File file = new File(tableName + ".json");
        if (file.exists()) {
            file.delete();
        }
    }
}
