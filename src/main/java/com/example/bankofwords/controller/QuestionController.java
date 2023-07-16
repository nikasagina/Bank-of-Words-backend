package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.Random;

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
    public ResponseEntity<?> start(@PathVariable(value = "tableId") long tableId) {
        return ResponseEntity.ok(questionService.start(tableId, new Random().nextDouble()));
    }

    @GetMapping("/spelling/{tableId}")
    public ResponseEntity<?> spelling(@PathVariable(value = "tableId") long tableId) {
        return ResponseEntity.ok(questionService.spelling(tableId));
    }

    @GetMapping("/image/{tableId}")
    public ResponseEntity<?> image(@PathVariable(value = "tableId") long tableId) {
        return ResponseEntity.ok(questionService.image(tableId));
    }

    @PostMapping("/answer")
    public ResponseEntity<?> answer(@RequestParam("guess") String guess,
                                    @RequestParam("id") long flashcard_id) {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        Map<String, Object> response = questionService.answer(userId, guess, flashcard_id);
        if (response == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(response);
    }
}