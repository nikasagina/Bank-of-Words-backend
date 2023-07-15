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


    public boolean uploadWord(long tableId, String word, String definition, MultipartFile image) {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        if (tableDAO.containsWordAndDefinition(tableId, word, definition))
            return false;

        wordDAO.addWord(tableId, word, definition);

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

        return true;
    }

    public Map<String, Object> uploadBook(long tableId, MultipartFile file) {
        try {
            String fileExtension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
            List<String> unknownWords;

            if (fileExtension.equals("pdf")) {
                unknownWords = pdfParser.parsePdf(file);
            } else {
                return Map.of("error", "Unsupported file format.");
            }

            Map<String, String> addedWords = new HashMap<>();

            unknownWords.forEach(word -> {
                if (!tableDAO.containsWord(tableId, word) && !wordUtil.isSimple(word) && lexiconDAO.contains(word)) {
                    String definition = lexiconDAO.getWordDefinition(word);
                    addedWords.put(word, definition);
                    wordDAO.addWord(tableId, word, definition);
                }
            });

            return Map.of("added_words", addedWords);
        } catch (IOException e) {
            return Map.of("error", "Failed to parse the book file.");
        }
    }


    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return lastIndex == -1 ? "" : fileName.substring(lastIndex + 1).toLowerCase();
    }
}
