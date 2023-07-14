package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return wordService.learn(wordId);
    }

    @DeleteMapping("/delete/{wordId}")
    public ResponseEntity<?> delete(@PathVariable("wordId") long wordId) {
        return wordService.delete(wordId);
    }

    @GetMapping("/definitions/{word}")
    public ResponseEntity<?> getDefinitions(@PathVariable("word") String word) {
        return wordService.getDefinitions(word);
    }

    @GetMapping("/info/{word}")
    public ResponseEntity<?> getWordInfo(@PathVariable("word") String word) {
        return wordService.getWordInfo(word);
    }
}
