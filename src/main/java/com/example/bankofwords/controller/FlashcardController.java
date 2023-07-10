package com.example.bankofwords.controller;

import com.example.bankofwords.service.FlashcardService;
import com.example.bankofwords.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/flashcard")
public class FlashcardController {

    private final FlashcardService flashcardService;

    @Autowired
    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @GetMapping("/front/text/{tableId}")
    public ResponseEntity<?> getTextFront(@RequestHeader("Authorization") String authHeader,
                                   @PathVariable(value = "tableId") long tableId) {
        return flashcardService.getTextFront(authHeader, tableId);
    }


    @GetMapping("/front/image/{tableId}")
    public ResponseEntity<?> getImageFront(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable(value = "tableId") long tableId) {
        return flashcardService.getImageFront(authHeader, tableId);
    }

    @GetMapping("/back/{flashcardId}")
    public ResponseEntity<?> getImageBack(@RequestHeader("Authorization") String authHeader,
                                          @PathVariable(value = "flashcardId") long flashcardId) {
        return flashcardService.getFlashcardBack(authHeader, flashcardId);
    }
}
