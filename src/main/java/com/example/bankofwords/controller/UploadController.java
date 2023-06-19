package com.example.bankofwords.controller;

import com.example.bankofwords.dao.StatisticsDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/upload")
public class UploadController {

    private final WordDAO wordDAO;
    private final UserDAO userDAO;
    private final JwtUtil jwtUtil;

    @Autowired
    public UploadController(WordDAO wordDAO, UserDAO userDAO, JwtUtil jwtUtil) {
        this.wordDAO = wordDAO;
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/word")
    public ResponseEntity<?> uploadWord(@RequestHeader("Authorization") String authHeader,
                           @RequestParam("word") String word,
                           @RequestParam("definition") String definition) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            if (wordDAO.userWordsContain(userId, word)) {
                response.put("successful", false);
            } else {
                wordDAO.addWord(userId, word, definition);
                response.put("successful", true);
            }

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}