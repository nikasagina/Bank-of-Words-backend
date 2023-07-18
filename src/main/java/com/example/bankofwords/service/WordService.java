package com.example.bankofwords.service;

import com.example.bankofwords.dao.LexiconDAO;
import com.example.bankofwords.dao.WordDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class WordService {
    private final WordDAO wordDAO;
    private final LexiconDAO lexiconDAO;

    @Autowired
    public WordService(WordDAO wordDAO, LexiconDAO lexiconDAO) {
        this.wordDAO = wordDAO;
        this.lexiconDAO = lexiconDAO;
    }

    public boolean learn(long wordId, long userId) {
        if (wordDAO.alreadyKnows(userId, wordId))
            return false;

        wordDAO.learnWord(userId, wordId);
        return true;
    }

    public void delete(long wordId) {
        wordDAO.deleteWord(wordId);
    }

    public List<String> getDefinitions(String word) {
        return lexiconDAO.getDefinitions(word);
    }

    public Map<String, Object> getWordInfo(String word) {
        Map<String, Object> response = new HashMap<>();

        String definition = lexiconDAO.getWordDefinition(word);

        if (Objects.equals(definition, "")) {
            log.info("Internal dictionary does not contain the word: {}", word);
            return null;
        }

        List<String> otherDefinitions = lexiconDAO.getDefinitions(word);
        otherDefinitions.remove(0); // guaranteed that list's size is at least 1

        response.put("word", word);
        response.put("definition", definition);
        response.put("other_definitions", otherDefinitions);
        response.put("examples", lexiconDAO.getDefinitionExamples(word, definition));
        response.put("synonyms", lexiconDAO.getWordSynonyms(word));
        response.put("antonyms", lexiconDAO.getWordAntonyms(word));

        return response;
    }
}
