package com.example.bankofwords.service;

import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.utils.JwtUtil;
import com.example.bankofwords.utils.AuthValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private AuthValidator authValidator;

    @Mock
    private JwtUtil jwtUtil;


    @Test
    void testRegisterUser_SuccessfulRegistration() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";

        when(authValidator.checkRegisterErrors(username, password, email)).thenReturn(List.of());

        // Act
        ResponseEntity<?> response = authService.registerUser(username, password, email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertTrue((Boolean) responseBody.get("successful"));
    }

    @Test
    void testRegisterUser_RegistrationErrors() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        String email = "test@example.com";
        List<String> errors = List.of("Username is not available", "Already registered with this Email");

        when(authValidator.checkRegisterErrors(username, password, email)).thenReturn(errors);

        // Act
        ResponseEntity<?> response = authService.registerUser(username, password, email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(false, responseBody.get("successful"));
        assertEquals("Username is not available", responseBody.get("usernameErrorClass"));
        assertNull(responseBody.get("passwordErrorClass"));
        assertEquals("Already registered with this Email", responseBody.get("emailErrorClass"));
    }

    @Test
    void testAuthenticate_ValidCredentials() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        String jwtToken = "testJwtToken";

        when(authValidator.validLogin(username, password)).thenReturn(true);
        when(jwtUtil.generateToken(username)).thenReturn(jwtToken);

        // Act
        ResponseEntity<?> response = authService.authenticate(username, password);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(jwtToken, responseBody.get("token"));
    }

    @Test
    void testAuthenticate_InvalidCredentials() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";

        when(authValidator.validLogin(username, password)).thenReturn(false);

        // Act
        ResponseEntity<?> response = authService.authenticate(username, password);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Invalid login credentials.", responseBody.get("error"));
    }

    @Test
    void testLogout_ValidToken() {
        // Arrange
        String authHeader = "Bearer testToken";
        String token = "testToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(jwtUtil.validateToken(token, username)).thenReturn(true);

        // Act
        ResponseEntity<?> response = authService.logout(authHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLogout_InvalidToken() {
        // Arrange
        String authHeader = "Bearer testToken";
        String token = "testToken";
        String username = "testUser";

        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(jwtUtil.validateToken(token, username)).thenReturn(false);

        // Act
        ResponseEntity<?> response = authService.logout(authHeader);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Invalid Token", responseBody.get("error"));
    }
}
