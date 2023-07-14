package com.example.bankofwords.service;

import com.example.bankofwords.dao.LexiconDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class WordService {
    private final WordDAO wordDAO;
    private final LexiconDAO lexiconDAO;

    @Autowired
    public WordService(WordDAO wordDAO, LexiconDAO lexiconDAO) {
        this.wordDAO = wordDAO;
        this.lexiconDAO = lexiconDAO;
    }

    public ResponseEntity<?> learn(long wordId) {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        Map<String, Object> response = new HashMap<>();

        if (wordDAO.alreadyKnows(userId, wordId)) {
            response.put("success", false);
        } else {
            wordDAO.learnWord(userId, wordId);
            response.put("success", true);
        }

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> delete(long wordId) {
        wordDAO.deleteWord(wordId);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getDefinitions(String word) {

        Map<String, Object> response = new HashMap<>();

        List<String> definitions = lexiconDAO.getDefinitions(word);

        response.put("available_definitions", definitions);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getWordInfo(String word) {
        Map<String, Object> response = new HashMap<>();

        String definition = lexiconDAO.getWordDefinition(word);

        if (Objects.equals(definition, "")) {
            return ResponseEntity.noContent().build();
        }


        List<String> otherDefinitions = lexiconDAO.getDefinitions(word);
        otherDefinitions.remove(0); // guaranteed that list's size is at least 1

        response.put("word", word);
        response.put("definition", definition);
        response.put("other_definitions", otherDefinitions);
        response.put("examples", lexiconDAO.getDefinitionExamples(word, definition));
        response.put("synonyms", lexiconDAO.getWordSynonyms(word));
        response.put("antonyms", lexiconDAO.getWordAntonyms(word));

        return ResponseEntity.ok(response);
    }
}
