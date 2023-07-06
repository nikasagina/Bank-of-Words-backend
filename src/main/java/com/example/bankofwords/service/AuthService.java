package com.example.bankofwords.service;

import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.utils.JwtUtil;
import com.example.bankofwords.utils.SecurityUtils;
import com.example.bankofwords.utils.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;

@Service
public class AuthService {
    private final UserDAO userDAO;
    private final AuthValidator authValidator;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserDAO userDAO, AuthValidator authValidator, JwtUtil jwtUtil) {
        this.userDAO = userDAO;
        this.authValidator = authValidator;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, Object> registerUser(String username, String password, String email) {
        Map<String, Object> response = new HashMap<>();
        List<String> errors = authValidator.checkRegisterErrors(username, password, email);

        if (!errors.isEmpty()) {
            response.put("successful", false);
            response.put("usernameErrorClass", authValidator.getRegisterErrorClass("username", errors));
            response.put("passwordErrorClass", authValidator.getRegisterErrorClass("password", errors));
            response.put("emailErrorClass", authValidator.getRegisterErrorClass("email", errors));
            return response;
        }

        String hash = SecurityUtils.hashPassword(password);
        boolean success = userDAO.addUser(username, hash, email);
        response.put("successful", success);

        if (!success) {
            throw new AuthServiceException("Failed to add user.");
        }

        return response;
    }

    public Map<String, Object> authenticate(String username, String password) {
        if (authValidator.validLogin(username, password)) {
            String jwt = jwtUtil.generateToken(username);

            return Map.of("token", jwt);
        } else {
            throw new AuthServiceException("Invalid login credentials.");
        }
    }

    public void logout(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            jwtUtil.invalidateToken(token);
        } else {
            throw new AuthServiceException("Invalid token.");
        }
    }

    public static class AuthServiceException extends RuntimeException {
        public AuthServiceException(String message) {
            super(message);
        }
    }
}