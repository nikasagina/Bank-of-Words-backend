package com.example.bankofwords.controller;


import com.example.bankofwords.dao.LexiconDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.parser.EpubParser;
import com.example.bankofwords.parser.PdfParser;
import com.example.bankofwords.parser.MobiParser;
import com.example.bankofwords.utils.JwtUtil;
import com.example.bankofwords.utils.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final MobiParser mobiParser;
    private final EpubParser epubParser;

    @Autowired
    public UploadController(WordDAO wordDAO, UserDAO userDAO, JwtUtil jwtUtil, LexiconDAO lexiconDAO, WordUtil wordUtil,
                            WordUtil wordUtil1, PdfParser pdfParser, MobiParser mobiParser, EpubParser epubParser) {
        this.wordDAO = wordDAO;
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
        this.lexiconDAO = lexiconDAO;
        this.wordUtil = wordUtil1;
        this.pdfParser = pdfParser;
        this.mobiParser = mobiParser;
        this.epubParser = epubParser;
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

                switch (fileExtension) {
                    case "pdf" -> unknownWords = pdfParser.parsePdf(file);
//                    case "mobi" -> unknownWords = mobiParser.parseMobi(file);
//                    case "epub" -> unknownWords = epubParser.parseEpub(file);
                    default -> {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Unsupported file format.");
                    }
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
                response.put("added words", addedWords);

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
