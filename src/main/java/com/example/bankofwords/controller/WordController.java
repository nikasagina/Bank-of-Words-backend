package com.example.bankofwords.controller;


import com.example.bankofwords.dao.LexiconDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/word")
public class WordController {
    private final WordDAO wordDAO;
    private final UserDAO userDAO;
    private final JwtUtil jwtUtil;
    private final LexiconDAO lexiconDAO;

    @Autowired
    public WordController(WordDAO wordDAO, UserDAO userDAO, JwtUtil jwtUtil, LexiconDAO lexiconDAO) {
        this.wordDAO = wordDAO;
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
        this.lexiconDAO = lexiconDAO;
    }


    @PostMapping("/learn")
    public ResponseEntity<?> learn(@RequestHeader("Authorization") String authHeader, @RequestParam("word") String word) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            long wordId = wordDAO.getWordId(word, userId);

            if (wordDAO.alreadyKnows(userId, wordId)) {
                response.put("success", false);
            } else {
                wordDAO.learnWord(userId, wordId);
                response.put("success", true);
            }

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/definitions")
    public ResponseEntity<?> getDefinitions(@RequestHeader("Authorization") String authHeader,
                                            @RequestParam("word") String word) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            List<String> definitions = lexiconDAO.getDefinitions(word);

            response.put("available definitions", definitions);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> getWordInfo(@RequestHeader("Authorization") String authHeader,
                                            @RequestParam("word") String word) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            String definition = lexiconDAO.getWordDefinition(word);

            if (Objects.equals(definition, "")) {
                return ResponseEntity.noContent().build();
            }


            List<String> otherDefinitions = lexiconDAO.getDefinitions(word);
            otherDefinitions.remove(0); // guaranteed that list's size is at least 1

            response.put("word", word);
            response.put("definition", definition);
            response.put("other definitions", otherDefinitions);
            response.put("examples", lexiconDAO.getDefinitionExamples(word, definition));
            response.put("synonyms", lexiconDAO.getWordSynonyms(word));
            response.put("antonyms", lexiconDAO.getWordAntonyms(word));

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/learning")
    public ResponseEntity<?> getAllLearningWords(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            List<Word> words = wordDAO.getAllLearningWords(userId);

            response.put("learning words", words);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/learned")
    public ResponseEntity<?> getAllLearnedWords(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            List<Word> words = wordDAO.getAllLearnedWords(userId);

            response.put("learned words", words);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
