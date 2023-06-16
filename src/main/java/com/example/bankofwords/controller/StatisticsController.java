package com.example.bankofwords.controller;


import com.example.bankofwords.dao.StatisticsDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/stats")
public class StatisticsController {
    private final WordDAO wordDAO;
    private final UserDAO userDAO;
    private final JwtUtil jwtUtil;
    private final StatisticsDAO statisticsDAO;

    @Autowired
    public StatisticsController(WordDAO wordDAO, UserDAO userDAO, StatisticsDAO statisticsDAO, JwtUtil jwtUtil) {
        this.wordDAO = wordDAO;
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
        this.statisticsDAO = statisticsDAO;
    }

    @GetMapping("/user/word-rate")
    public ResponseEntity<?> start(@RequestHeader("Authorization") String authHeader, @RequestParam("word") String word) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            long wordId = wordDAO.getWordId(word, userId);

            response.put("rate", statisticsDAO.getUserSuccessRateForWord(userId, wordId));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/user/count/total-guesses")
    public ResponseEntity<?> userTotalGuessesCount(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);

            response.put("count", statisticsDAO.getUserTotalGuessesCount(userId));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/user/count/success-guesses")
    public ResponseEntity<?> userSuccessGuessesCount(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);

            response.put("count", statisticsDAO.getUserSuccessfulGuessesCount(userId));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/user/success-rate")
    public ResponseEntity<?> userSuccessRate(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);

            response.put("rate", statisticsDAO.getUserSuccessRateTotal(userId));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/user/word/most-guessed")
    public ResponseEntity<?> mostGuessedByUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            long wordId = statisticsDAO.getMostGuessedWordByUser(userId);
            Word word = wordDAO.getWordWithId(wordId);

            response.put("word", word.getWord());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/user/word/least-guessed")
    public ResponseEntity<?> leastGuessedByUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            long wordId = statisticsDAO.getLeastGuessedWordByUser(userId);
            Word word = wordDAO.getWordWithId(wordId);

            response.put("word", word.getWord());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/most-guessed")
    public ResponseEntity<?> mostGuessedOverall(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long wordId = statisticsDAO.getMostGuessedWordOverall();
            Word word = wordDAO.getWordWithId(wordId);

            response.put("word", word.getWord());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/least-guessed")
    public ResponseEntity<?> leastGuessedOverall(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long wordId = statisticsDAO.getLeastGuessedWordOverall();
            Word word = wordDAO.getWordWithId(wordId);

            response.put("word", word.getWord());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/top-user")
    public ResponseEntity<?> topUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = statisticsDAO.getTopUserIdsWithBestSuccessRate(1).get(0);
            String topUsername = userDAO.getUsername(userId);

            response.put("word", topUsername);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/top-5")
    public ResponseEntity<?> top5(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            List<Long> userIds = statisticsDAO.getTopUserIdsWithBestSuccessRate(5);
            List<String> topUsernames = new ArrayList<>();

            for( long userId : userIds) {
                topUsernames.add(userDAO.getUsername(userId));
            }

            response.put("word", topUsernames);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}