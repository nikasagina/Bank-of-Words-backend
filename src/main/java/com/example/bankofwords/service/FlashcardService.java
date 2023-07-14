package com.example.bankofwords.service;

import com.example.bankofwords.dao.*;
import com.example.bankofwords.objects.Image;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.singletons.FlashcardAnswers;
import com.example.bankofwords.singletons.UniqueIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FlashcardService {

    private final WordDAO wordDAO;
    private final ImageDAO imageDAO;

    @Autowired
    public FlashcardService(WordDAO wordDAO, ImageDAO imageDAO) {
        this.wordDAO = wordDAO;
        this.imageDAO = imageDAO;
    }

    public ResponseEntity<?> getTextFront(long tableId) {
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
    }

    public ResponseEntity<?> getImageFront(long tableId) {
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
    }

    public ResponseEntity<?> getFlashcardBack(long flashcardId) {
        Map<String, Object> response = new HashMap<>();

        if(!FlashcardAnswers.getInstance().contains(flashcardId)) {
            return ResponseEntity.notFound().build();
        }

        Word word = FlashcardAnswers.getInstance().getAnswer(flashcardId);
        FlashcardAnswers.getInstance().remove(flashcardId);

        response.put("back", word.getWord());

        return ResponseEntity.ok(response);
    }
}
