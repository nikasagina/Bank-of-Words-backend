package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/flashcard")
@Secure
public class FlashcardController {

    private final FlashcardService flashcardService;

    @Autowired
    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @GetMapping("/front/text/{tableId}")
    public ResponseEntity<?> getTextFront(@PathVariable(value = "tableId") long tableId) {
        return ResponseEntity.ok(flashcardService.getTextFront(tableId));
    }


    @GetMapping("/front/image/{tableId}")
    public ResponseEntity<?> getImageFront(@PathVariable(value = "tableId") long tableId) {
        return ResponseEntity.ok(flashcardService.getImageFront(tableId));
    }

    @GetMapping("/back/{flashcardId}")
    public ResponseEntity<?> getFlashcardBack(@PathVariable(value = "flashcardId") long flashcardId) {
        String answer = flashcardService.getFlashcardBack(flashcardId);

        return answer == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(Map.of("back", answer));
    }
}
