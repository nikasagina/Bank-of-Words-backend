package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/word")
@Secure
public class WordController {
    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @PostMapping("/learn/{wordId}")
    public ResponseEntity<?> learn(@PathVariable("wordId") long wordId) {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        return ResponseEntity.ok(Map.of("success", wordService.learn(wordId, userId)));
    }

    @DeleteMapping("/delete/{wordId}")
    public void delete(@PathVariable("wordId") long wordId) {
        wordService.delete(wordId);
    }

    @GetMapping("/definitions/{word}")
    public ResponseEntity<?> getDefinitions(@PathVariable("word") String word) {
        return ResponseEntity.ok(Map.of("available_definitions", wordService.getDefinitions(word)));
    }

    @GetMapping("/info/{word}")
    public ResponseEntity<?> getWordInfo(@PathVariable("word") String word) {
        Map<String, Object> response = wordService.getWordInfo(word);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }
}
