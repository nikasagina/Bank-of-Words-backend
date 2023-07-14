package com.example.bankofwords.service;

import com.example.bankofwords.dao.*;
import com.example.bankofwords.parser.PdfParser;
import com.example.bankofwords.utils.JwtUtil;
import com.example.bankofwords.utils.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UploadService {
    private final WordDAO wordDAO;
    private final LexiconDAO lexiconDAO;
    private final WordUtil wordUtil;
    private final PdfParser pdfParser;
    private final ImageDAO imageDAO;
    private final TableDAO tableDAO;

    @Autowired
    public UploadService(WordDAO wordDAO, LexiconDAO lexiconDAO, WordUtil wordUtil,
                         PdfParser pdfParser, ImageDAO imageDAO, TableDAO tableDAO) {
        this.wordDAO = wordDAO;
        this.lexiconDAO = lexiconDAO;
        this.wordUtil = wordUtil;
        this.pdfParser = pdfParser;
        this.imageDAO = imageDAO;
        this.tableDAO = tableDAO;
    }


    public ResponseEntity<?> uploadWord(long tableId, String word, String definition, MultipartFile image) {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        Map<String, Object> response = new HashMap<>();

        if (tableDAO.containsWordAndDefinition(tableId, word, definition)) {
            response.put("successful", false);
        } else {
            wordDAO.addWord(tableId, word, definition);
            response.put("successful", true);

            long wordId = wordDAO.getWordId(word, definition, userId);
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
    }

    public ResponseEntity<?> addImageToWord(long tableId, long wordId, MultipartFile image) {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        if (image != null && !image.isEmpty()) {
            // if the user want to add picture to the initial word, copy the initial word to the users words
            if (wordDAO.getWordCreator(wordId) == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can not add an image to an initial word");
            }

            String imageName = userId + "-" + UUID.randomUUID() + ".jpg";
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
    }

    public ResponseEntity<?> uploadBook(long tableId, MultipartFile file) {
        try {
            String fileExtension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
            List<String> unknownWords;

            if (fileExtension.equals("pdf")) {
                unknownWords = pdfParser.parsePdf(file);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Unsupported file format.");
            }

            Map<String, String> addedWords = new HashMap<>();

            unknownWords.forEach(word -> {
                if (!tableDAO.containsWord(tableId, word) && !wordUtil.isSimple(word) && lexiconDAO.contains(word)) {
                    String definition = lexiconDAO.getWordDefinition(word);
                    addedWords.put(word, definition);
                    wordDAO.addWord(tableId, word, definition);
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
    }


    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return lastIndex == -1 ? "" : fileName.substring(lastIndex + 1).toLowerCase();
    }
}
