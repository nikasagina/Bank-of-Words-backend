package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.FlashcardService;
import com.example.bankofwords.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return flashcardService.getTextFront(tableId);
    }


    @GetMapping("/front/image/{tableId}")
    public ResponseEntity<?> getImageFront(@PathVariable(value = "tableId") long tableId) {
        return flashcardService.getImageFront(tableId);
    }

    @GetMapping("/back/{flashcardId}")
    public ResponseEntity<?> getImageBack(@PathVariable(value = "flashcardId") long flashcardId) {
        return flashcardService.getFlashcardBack(flashcardId);
    }
}
