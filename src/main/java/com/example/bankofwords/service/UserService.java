package com.example.bankofwords.service;

import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.User;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserDAO userDAO;
    private final JwtUtil jwtUtil;
    private final WordDAO wordDAO;

    @Autowired
    public UserService(UserDAO userDAO, JwtUtil jwtUtil, WordDAO wordDAO) {
        this.wordDAO = wordDAO;
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> getInfo(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            User user = userDAO.getUserByUsername(username);
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("joinDate", user.getFormattedJoinDate());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> getAllLearningWords(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            List<Word> words = wordDAO.getAllLearningWords(userId);

            response.put("learning_words", words);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> getAllLearnedWords(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            List<Word> words = wordDAO.getAllLearnedWords(userId);

            response.put("learned_words", words);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
