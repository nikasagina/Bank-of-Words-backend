package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/question")
@Secure
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/default/{tableId}")
    public ResponseEntity<?> start(@PathVariable(value = "tableId") Long tableId) {
        return questionService.start(tableId);
    }

    @GetMapping("/spelling/{tableId}")
    public ResponseEntity<?> spelling(@PathVariable(value = "tableId") Long tableId) {
        return questionService.spelling(tableId);
    }

    @GetMapping("/image/{tableId}")
    public ResponseEntity<?> image(@PathVariable(value = "tableId") Long tableId) {
        return questionService.image(tableId);
    }

    @PostMapping("/answer")
    public ResponseEntity<?> answer(@RequestParam("guess") String guess,
                                    @RequestParam("id") long flashcard_id) {
        return questionService.answer(guess, flashcard_id);
    }
}