package com.example.bankofwords.service;

import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.utils.JwtUtil;
import com.example.bankofwords.utils.SecurityUtils;
import com.example.bankofwords.utils.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuthService {
    private final UserDAO userDAO;
    private final AuthValidator authValidator;
    private final JwtUtil jwtUtil;
    private final SecurityUtils securityUtils;

    @Autowired
    public AuthService(UserDAO userDAO, AuthValidator authValidator, JwtUtil jwtUtil, SecurityUtils securityUtils) {
        this.userDAO = userDAO;
        this.authValidator = authValidator;
        this.jwtUtil = jwtUtil;
        this.securityUtils = securityUtils;
    }

    public Map<String, Object> registerUser(String username, String password, String email) {
        Map<String, Object> response = new HashMap<>();
        List<String> errors = authValidator.checkRegisterErrors(username, password, email);

        if (!errors.isEmpty()) {
            response.put("successful", false);
            response.put("usernameErrorClass", AuthValidator.getRegisterErrorClass("username", errors));
            response.put("passwordErrorClass", AuthValidator.getRegisterErrorClass("password", errors));
            response.put("emailErrorClass", AuthValidator.getRegisterErrorClass("email", errors));
            return response;
        }

        String hash = securityUtils.hashPassword(password);
        userDAO.addUser(username, hash, email);

        response.put("successful", true);
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