package com.example.bankofwords.controller;

import com.example.bankofwords.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/word")
public class WordController {
    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @PostMapping("/learn/{wordId}")
    public ResponseEntity<?> learn(@RequestHeader("Authorization") String authHeader,
                                   @PathVariable("wordId") long wordId) {
        return wordService.learn(authHeader, wordId);
    }

    @DeleteMapping("/delete/{wordId}")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String authHeader,
                                    @PathVariable("wordId") long wordId) {
        return wordService.delete(authHeader, wordId);
    }

    @GetMapping("/definitions/{word}")
    public ResponseEntity<?> getDefinitions(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable("word") String word) {
        return wordService.getDefinitions(authHeader, word);
    }

    @GetMapping("/info/{word}")
    public ResponseEntity<?> getWordInfo(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable("word") String word) {
        return wordService.getWordInfo(authHeader, word);
    }
}
