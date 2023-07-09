package com.example.bankofwords.service;

import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.utils.JwtUtil;
import com.example.bankofwords.utils.SecurityUtils;
import com.example.bankofwords.utils.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    public ResponseEntity<?> registerUser(String username, String password, String email) {
        Map<String, Object> response = new HashMap<>();
        List<String> errors = authValidator.checkRegisterErrors(username, password, email);

        if (!errors.isEmpty()) {
            response.put("successful", false);
            response.put("usernameErrorClass", authValidator.getRegisterErrorClass("username", errors));
            response.put("passwordErrorClass", authValidator.getRegisterErrorClass("password", errors));
            response.put("emailErrorClass", authValidator.getRegisterErrorClass("email", errors));
            return ResponseEntity.ok(response);
        }

        String hash = SecurityUtils.hashPassword(password);
        boolean success = userDAO.addUser(username, hash, email);
        response.put("successful", success);

        if (!success) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("successful", false, "error", "Failed to add user."));
        }

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> authenticate(String username, String password) {
        if (authValidator.validLogin(username, password)) {
            String jwt = jwtUtil.generateToken(username);

            return ResponseEntity.ok(Map.of("token", jwt));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid login credentials."));
        }
    }

    public ResponseEntity<?> logout(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            jwtUtil.invalidateToken(token);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid Token"));
        }
    }
}