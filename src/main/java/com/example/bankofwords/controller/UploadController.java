package com.example.bankofwords.controller;


import com.example.bankofwords.dao.ImageDAO;
import com.example.bankofwords.dao.LexiconDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.parser.PdfParser;
import com.example.bankofwords.utils.JwtUtil;
import com.example.bankofwords.utils.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("api/upload")
public class UploadController {

    private final WordDAO wordDAO;
    private final UserDAO userDAO;
    private final JwtUtil jwtUtil;
    private final LexiconDAO lexiconDAO;
    private final WordUtil wordUtil;
    private final PdfParser pdfParser;
    private final ImageDAO imageDAO;

    @Autowired
    public UploadController(WordDAO wordDAO, UserDAO userDAO, JwtUtil jwtUtil, LexiconDAO lexiconDAO,
                            WordUtil wordUtil, PdfParser pdfParser, ImageDAO imageDAO) {
        this.wordDAO = wordDAO;
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
        this.lexiconDAO = lexiconDAO;
        this.wordUtil = wordUtil;
        this.pdfParser = pdfParser;
        this.imageDAO = imageDAO;
    }

    @PostMapping("/word")
    public ResponseEntity<?> uploadWord(@RequestHeader("Authorization") String authHeader,
                                        @RequestParam("word") String word,
                                        @RequestParam("definition") String definition,
                                        @RequestParam(value = "image", required = false) MultipartFile image) {
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

                long wordId = wordDAO.getWordId(word, userId);
                if (image != null && !image.isEmpty()) {
                    String imageName = word + "_" + UUID.randomUUID() + ".jpg";
                    Path imagePath = Paths.get("src/main/resources/static/images/" + imageName);

                    try {
                        Files.write(imagePath, image.getBytes());
                        imageDAO.addImage(wordId, imageName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/word/image")
    public ResponseEntity<?> addImageToWord(@RequestHeader("Authorization") String authHeader,
                                            @RequestParam("word") String word,
                                            @RequestParam("image") MultipartFile image) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            if (image != null && !image.isEmpty()) {
                long userId = userDAO.getUserID(username);
                Long wordId = wordDAO.getWordId(word, userId);

                if (wordId == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Word not found");
                }

                // if the user want to add picture to the initial word, copy the initial word to the users words
                if (wordDAO.getWordCreator(wordId) == 0) {
                    wordDAO.addWord(userId, word, wordDAO.getWordWithId(wordId).getDefinition());
                    wordId = wordDAO.getWordId(word, userId);
                }

                String imageName = userId + "_" + word + "_" + UUID.randomUUID() + ".jpg";
                Path imagePath = Paths.get("src/main/resources/static/images/" + imageName);
                try {
                    Files.write(imagePath, image.getBytes());
                    imageDAO.addImage(wordId, imageName);
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save the image.");
                }

                return ResponseEntity.ok("Image added successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No image provided.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/book")
    public ResponseEntity<?> uploadBook(@RequestHeader("Authorization") String authHeader,
                                        @RequestParam("file") MultipartFile file) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            try {
                String fileExtension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
                List<String> unknownWords;
                long userId = userDAO.getUserID(username);

                if (fileExtension.equals("pdf")) {
                    unknownWords = pdfParser.parsePdf(file);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Unsupported file format.");
                }

                Map<String, String> addedWords = new HashMap<>();

                unknownWords.forEach(word -> {
                    if (!wordDAO.userWordsContain(userId, word) && !wordUtil.isSimple(word) && lexiconDAO.contains(word)) {
                        String definition = lexiconDAO.getWordDefinition(word);
                        addedWords.put(word, definition);
                        wordDAO.addWord(userId, word, definition);
                    }
                });

                Map<String, Object> response = new HashMap<>();
                response.put("successful", true);
                response.put("message", "Words from the book have been added.");
                response.put("added_words", addedWords);

                return ResponseEntity.ok(response);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to parse the book file.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return lastIndex == -1 ? "" : fileName.substring(lastIndex + 1).toLowerCase();
    }
}
