package com.example.bankofwords.service;

import com.example.bankofwords.constants.StatisticsConstants;
import com.example.bankofwords.dao.ImageDAO;
import com.example.bankofwords.dao.StatisticsDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.dao.WordHistoryDAO;
import com.example.bankofwords.objects.Image;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.singletons.FlashcardAnswers;
import com.example.bankofwords.singletons.UniqueIdGenerator;
import com.example.bankofwords.utils.IncorrectWordHeuristics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class QuestionService {

    private final WordDAO wordDAO;
    private final StatisticsDAO statisticsDAO;
    private final ImageDAO imageDAO;
    private final WordHistoryDAO wordHistoryDAO;

    @Autowired
    public QuestionService(WordDAO wordDAO, StatisticsDAO statisticsDAO, ImageDAO imageDAO, WordHistoryDAO wordHistoryDAO) {
        this.wordDAO = wordDAO;
        this.imageDAO = imageDAO;
        this.statisticsDAO = statisticsDAO;
        this.wordHistoryDAO = wordHistoryDAO;
    }

    public Map<String, Object> start(Long tableId, double randNum) {
        Word correct = null; // use algorithm to generate word to serve

        if (randNum < StatisticsConstants.LEARNING_WORD_SERVE_RATE){
            correct = wordDAO.getRandomWordWithProgressFromTable(tableId);
        }

        // handles both cases, where no progress in the table and when random condition is false
        if (correct == null) {
            correct = wordDAO.getRandomWordFromTable(tableId);
        }

        if (correct == null) {
            log.info("No words left to learn in tableId: {}", tableId);
            return Map.of("error", "No more words left to learn");
        }

        List<Word> choiceObjects = wordDAO.getIncorrectWordsFromTable(correct, tableId);

        choiceObjects.add(correct);
        Collections.shuffle(choiceObjects);

        List<String> choices = new ArrayList<>();
        for (Word word : choiceObjects) {
            choices.add(word.getWord());
        }

        return getFlashcardResponseEntity(correct, choices);
    }

    public Map<String, Object> spelling(Long tableId) {
        Word correct = wordDAO.getRandomWordFromTable(tableId);

        if (correct == null) {
            log.info("No words left to learn in tableId: {}", tableId);
            return Map.of("error", "No more words left to learn");
        }

        List<String> choiceStrings = IncorrectWordHeuristics.getIncorrectWords(correct.getWord());

        choiceStrings.add(correct.getWord());
        Collections.shuffle(choiceStrings);

        return getFlashcardResponseEntity(correct, choiceStrings);
    }

    public Map<String, Object> image(long tableId) {
        Image image = imageDAO.getRandomImageFromTable(tableId);

        if (image == null ) {
            log.info("No images found in tableId: {}", tableId);
            return Map.of("error", "No images found");
        }

        String filename = image.getImageName();
        Word word = wordDAO.getWordWithId(image.getWordId());

        long flashcardId = UniqueIdGenerator.getInstance().generateUniqueId();
        FlashcardAnswers.getInstance().add(flashcardId, word);

        return Map.of("id", flashcardId, "filename", filename);
    }

    public Map<String, Object> answer(long userId, String guess, long flashcard_id) {
        Map<String, Object> response = new HashMap<>();

        if(!FlashcardAnswers.getInstance().contains(flashcard_id)) {
            log.info("Call for removed or never existed flashcardId: {}", flashcard_id);
            return null;
        }

        Word correctAnswer = FlashcardAnswers.getInstance().getAnswer(flashcard_id);
        FlashcardAnswers.getInstance().remove(flashcard_id);

        long wordId = correctAnswer.getId();
        boolean isCorrect = correctAnswer.getWord().equalsIgnoreCase(guess);

        response.put("correct", isCorrect);
        response.put("answer", correctAnswer.getWord());
        response.put("wordId", correctAnswer.getId());

        wordHistoryDAO.recordAnswer(userId, wordId, isCorrect);
        if (isCorrect) {
            statisticsDAO.incrementCorrectAndTotal(userId, wordId);
        } else {
            statisticsDAO.incrementTotal(userId, wordId);
        }

        if (statisticsDAO.getTotalCount(userId, wordId) > StatisticsConstants.MINIMUM_TRIES_TO_LEARN &&
                statisticsDAO.getUserSuccessRateForWord(userId, wordId) > StatisticsConstants.MINIMUM_LEARN_RATE) {
            log.info("User learned word through algorithm, userId: {}, wordId: {}", userId, wordId);
            wordDAO.learnWord(userId, wordId);
        }

        return response;
    }

    private Map<String, Object> getFlashcardResponseEntity(Word correct, List<String> choiceStrings) {
        long flashcardId = UniqueIdGenerator.getInstance().generateUniqueId();
        FlashcardAnswers.getInstance().add(flashcardId, correct);

        Map<String, Object> response = new HashMap<>();
        response.put("id", flashcardId);
        response.put("question", correct.getDefinition());
        response.put("choices", choiceStrings);

        return response;
    }
}