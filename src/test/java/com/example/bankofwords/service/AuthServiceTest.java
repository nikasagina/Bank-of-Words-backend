package com.example.bankofwords.service;

import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.utils.JwtUtil;
import com.example.bankofwords.utils.AuthValidator;
import com.example.bankofwords.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
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

    @Mock
    private SecurityUtils securityUtils;


    @Test
    public void testRegisterUserWithErrors() {
        when(authValidator.checkRegisterErrors(anyString(), anyString(), anyString())).thenReturn(Collections.singletonList("error"));

        Map<String, Object> response = authService.registerUser("username", "password", "email");

        assertFalse((Boolean) response.get("successful"));
        verify(authValidator).checkRegisterErrors(anyString(), anyString(), anyString());
        verifyNoInteractions(userDAO);
    }

    @Test
    public void testRegisterUserSuccessfully() {
        when(authValidator.checkRegisterErrors(anyString(), anyString(), anyString())).thenReturn(Collections.emptyList());
        when(securityUtils.hashPassword(anyString())).thenReturn("hashedPassword");

        Map<String, Object> response = authService.registerUser("username", "password", "email");

        assertTrue((Boolean) response.get("successful"));
        verify(authValidator).checkRegisterErrors(anyString(), anyString(), anyString());
        verify(userDAO).addUser("username", "hashedPassword", "email");
    }

    @Test
    public void testAuthenticateSuccessfully() {
        when(authValidator.validLogin(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("token");

        Map<String, Object> response = authService.authenticate("username", "password");

        assertEquals("token", response.get("token"));
        verify(authValidator).validLogin(anyString(), anyString());
    }

    @Test
    public void testAuthenticateFailed() {
        when(authValidator.validLogin(anyString(), anyString())).thenReturn(false);

        assertThrows(AuthService.AuthServiceException.class, () -> authService.authenticate("username", "password"));
        verify(authValidator).validLogin(anyString(), anyString());
    }

    @Test
    public void testLogoutSuccessfully() {
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("username");
        when(jwtUtil.validateToken(anyString(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> authService.logout("Bearer token"));
        verify(jwtUtil).invalidateToken(anyString());
    }

    @Test
    public void testLogoutFailed() {
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("username");
        when(jwtUtil.validateToken(anyString(), anyString())).thenReturn(false);

        assertThrows(AuthService.AuthServiceException.class, () -> authService.logout("Bearer token"));
        verifyNoMoreInteractions(jwtUtil);
    }
}
