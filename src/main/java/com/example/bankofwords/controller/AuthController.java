package com.example.bankofwords.controller;

import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.utils.JwtUtil;
import com.example.bankofwords.utils.SecurityUtils;
import com.example.bankofwords.utils.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("api")
public class AuthController {
    private final UserDAO userDAO;
    private final AuthValidator authValidator;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserDAO userDAO, AuthValidator authValidator, JwtUtil jwtUtil) {
        this.userDAO = userDAO;
        this.authValidator = authValidator;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam("username") String username,
                                          @RequestParam("password") String password,
                                          @RequestParam("email") String email) {
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestParam("username") String username,
                                          @RequestParam("password") String password) {
        if (authValidator.validLogin(username, password)) {
            String jwt = jwtUtil.generateToken(username);
            Map<String, String> response = new HashMap<>();
            response.put("token", jwt);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> start(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {

            jwtUtil.invalidateToken(token);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}