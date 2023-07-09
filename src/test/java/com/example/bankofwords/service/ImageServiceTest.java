package com.example.bankofwords.service;

import com.example.bankofwords.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    void whenGetImageRequestWithValidToken_returnsOkResponse() throws IOException {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);

        // Act
        ResponseEntity<?> response = imageService.getImage(authHeader, "cat.jpg"); // cat.jpg must exist

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void whenGetImageRequestWithinValidToken_returnsUnauthorizedResponse() throws IOException {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(false);

        // Act
        ResponseEntity<?> response = imageService.getImage(authHeader, "");

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void whenImageDoesNotExist_assertThrowsIOException() {
        // Arrange
        String authHeader = "Bearer someToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken("someToken")).thenReturn(username);
        when(jwtUtil.validateToken("someToken", username)).thenReturn(true);

        // Act and Assert
        assertThrows(IOException.class, () -> imageService.getImage(authHeader, ""));
    }

}
