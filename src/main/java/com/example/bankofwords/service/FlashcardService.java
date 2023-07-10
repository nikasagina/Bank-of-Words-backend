package com.example.bankofwords.service;

import com.example.bankofwords.dao.*;
import com.example.bankofwords.objects.Image;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.singletons.FlashcardAnswers;
import com.example.bankofwords.singletons.UniqueIdGenerator;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FlashcardService {

    private final WordDAO wordDAO;
    private final JwtUtil jwtUtil;
    private final ImageDAO imageDAO;

    @Autowired
    public FlashcardService(WordDAO wordDAO, ImageDAO imageDAO, JwtUtil jwtUtil) {
        this.wordDAO = wordDAO;
        this.imageDAO = imageDAO;
        this.jwtUtil = jwtUtil;

    }

    public ResponseEntity<?> getTextFront(String authHeader, long tableId) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            Word word = wordDAO.getRandomWordFromTable(tableId);

            if (word == null) {
                response.put("error", "No more words left to learn");
                return ResponseEntity.ok(response);
            }

            long flashcardId = UniqueIdGenerator.getInstance().generateUniqueId();
            FlashcardAnswers.getInstance().add(flashcardId, word);

            response.put("id", flashcardId);
            response.put("frontText", word.getDefinition());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> getImageFront(String authHeader, long tableId) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            Image image = imageDAO.getRandomImageFromTable(tableId);

            if (image == null ) {
                response.put("error", "No images found");
                return ResponseEntity.ok(response);
            }

            String filename = image.getImageName();
            Word word = wordDAO.getWordWithId(image.getWordId());

            long flashcardId = UniqueIdGenerator.getInstance().generateUniqueId();
            FlashcardAnswers.getInstance().add(flashcardId, word);

            response.put("id", flashcardId);
            response.put("imageUrl", filename);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> getFlashcardBack(String authHeader, long flashcardId) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            if(!FlashcardAnswers.getInstance().contains(flashcardId)) {
                return ResponseEntity.notFound().build();
            }

            Word word = FlashcardAnswers.getInstance().getAnswer(flashcardId);
            FlashcardAnswers.getInstance().remove(flashcardId);

            response.put("back", word.getWord());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
