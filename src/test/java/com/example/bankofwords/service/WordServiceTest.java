package com.example.bankofwords.service;

import com.example.bankofwords.dao.LexiconDAO;
import com.example.bankofwords.dao.WordDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class WordServiceTest {

    @InjectMocks
    private WordService wordService;

    @Mock
    private WordDAO wordDAO;

    @Mock
    private LexiconDAO lexiconDAO;


    @Test
    public void learn_shouldReturnFalseIfAlreadyKnows() {
        // given
        long wordId = 1L;
        long userId = 1L;
        when(wordDAO.alreadyKnows(userId, wordId)).thenReturn(true);

        // when
        boolean result = wordService.learn(wordId, userId);

        // then
        assertFalse(result);
        verify(wordDAO, never()).learnWord(userId, wordId);
    }

    @Test
    public void learn_shouldReturnTrueAndLearnWord() {
        // given
        long wordId = 1L;
        long userId = 1L;
        when(wordDAO.alreadyKnows(userId, wordId)).thenReturn(false);

        // when
        boolean result = wordService.learn(wordId, userId);

        // then
        assertTrue(result);
        verify(wordDAO).learnWord(userId, wordId);
    }

    @Test
    public void deleteWord_shouldCallDeleteWordMethod() {
        // given
        long wordId = 1L;

        // when
        wordService.delete(wordId);

        // then
        verify(wordDAO).deleteWord(wordId);
    }

    @Test
    public void getDefinitions_shouldReturnDefinitions() {
        // given
        String word = "example";
        List<String> expected = List.of("a thing characteristic of its kind or illustrating a general rule", "a printed or written problem or exercise designed to illustrate a rule");

        when(lexiconDAO.getDefinitions(word)).thenReturn(expected);

        // when
        List<String> result = wordService.getDefinitions(word);

        // then
        assertEquals(expected, result);
    }

    @Test
    public void getWordInfo_shouldReturnWordInfo() {
        // given
        String word = "example";
        String definition = "a thing characteristic of its kind or illustrating a general rule";
        List<String> otherDefinitions = new ArrayList<>(List.of("a printed or written problem or exercise designed to illustrate a rule"));
        List<String> examples = new ArrayList<>(Arrays.asList("for example", "give an example", "set an example"));
        List<String> synonyms = new ArrayList<>(Arrays.asList("model", "exemplar", "typical case"));
        List<String> antonyms = new ArrayList<>(List.of("counterexample"));

        when(lexiconDAO.getWordDefinition(word)).thenReturn(definition);
        when(lexiconDAO.getDefinitions(word)).thenReturn(otherDefinitions);
        when(lexiconDAO.getDefinitionExamples(word, definition)).thenReturn(examples);
        when(lexiconDAO.getWordSynonyms(word)).thenReturn(synonyms);
        when(lexiconDAO.getWordAntonyms(word)).thenReturn(antonyms);

        Map<String, Object> expected = new HashMap<>();
        expected.put("word", word);
        expected.put("definition", definition);
        expected.put("other_definitions", otherDefinitions);
        expected.put("examples", examples);
        expected.put("synonyms", synonyms);
        expected.put("antonyms", antonyms);

        // when
        Map<String, Object> result = wordService.getWordInfo(word);

        // then
        assertEquals(expected, result);
    }

    @Test
    public void getWordInfo_shouldReturnNullIfNoDefinition() {
        // given
        String word = "example";

        when(lexiconDAO.getWordDefinition(word)).thenReturn("");

        // when
        Map<String, Object> result = wordService.getWordInfo(word);

        // then
        assertNull(result);
    }
}
