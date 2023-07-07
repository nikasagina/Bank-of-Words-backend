package com.example.bankofwords.controller;

import com.example.bankofwords.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/question")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/default/{tableId}")
    public ResponseEntity<?> start(@RequestHeader("Authorization") String authHeader,
                                   @PathVariable(value = "tableId", required = false) Long tableId) {
        return questionService.start(authHeader, tableId);
    }

    @GetMapping("/spelling/{tableId}")
    public ResponseEntity<?> spelling(@RequestHeader("Authorization") String authHeader,
                                      @PathVariable(value = "tableId", required = false) Long tableId) {
        return questionService.spelling(authHeader, tableId);
    }

    @GetMapping("/image/{tableId}")
    public ResponseEntity<?> image(@RequestHeader("Authorization") String authHeader,
                                   @PathVariable(value = "tableId", required = false) Long tableId) {
        return questionService.image(authHeader, tableId);
    }

    @PostMapping("/answer")
    public ResponseEntity<?> answer(@RequestHeader("Authorization") String authHeader,
                                    @RequestParam("guess") String guess,
                                    @RequestParam("id") long flashcard_id) {
        return questionService.answer(authHeader, guess, flashcard_id);
    }
}