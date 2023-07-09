package com.example.bankofwords.service;

import com.example.bankofwords.constants.StatisticsConstants;
import com.example.bankofwords.dao.*;
import com.example.bankofwords.objects.Image;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.singletons.FlashcardAnswers;
import com.example.bankofwords.singletons.UniqueIdGenerator;
import com.example.bankofwords.utils.IncorrectWordHeuristics;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionService {

    private final WordDAO wordDAO;
    private final UserDAO userDAO;
    private final JwtUtil jwtUtil;
    private final StatisticsDAO statisticsDAO;
    private final ImageDAO imageDAO;
    private final WordHistoryDAO wordHistoryDAO;

    @Autowired
    public QuestionService(WordDAO wordDAO, UserDAO userDAO, StatisticsDAO statisticsDAO, ImageDAO imageDAO,
                           JwtUtil jwtUtil, WordHistoryDAO wordHistoryDAO) {
        this.wordDAO = wordDAO;
        this.userDAO = userDAO;
        this.imageDAO = imageDAO;
        this.jwtUtil = jwtUtil;
        this.statisticsDAO = statisticsDAO;
        this.wordHistoryDAO = wordHistoryDAO;
    }

    public ResponseEntity<?> start(String authHeader, Long tableId) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();
            long userID = userDAO.getUserID(username);

            Word correct = null; // use algorithm to generate word to serve
            double randNum = new Random().nextDouble();
            if (randNum < StatisticsConstants.LEARNING_WORD_SERVE_RATE){
                if (tableId == null) {
                    correct = wordDAO.getRandomWordWithProgressFromAll(userID);
                } else {
                    correct = wordDAO.getRandomWordWithProgressFromTable(tableId);
                }
            }

            // handles both cases: 1. algorithm should generate random word and 2. user has no progress on any words
            if (correct == null){
                if (tableId == null) {
                    correct = wordDAO.getRandomWordFromAll(userID);
                } else {
                    correct = wordDAO.getRandomWordFromTable(tableId);
                }
            }

            if (correct == null) {
                response.put("error", "No more words left to learn");
                return ResponseEntity.ok(response);
            }

            List<Word> choiceObjects;
            if (tableId == null) {
                choiceObjects = wordDAO.getIncorrectWordsFromAll(correct, userID);
            } else {
                choiceObjects = wordDAO.getIncorrectWordsFromTable(correct, tableId);
            }

            choiceObjects.add(correct);
            Collections.shuffle(choiceObjects);

            List<String> choices = new ArrayList<>();
            for (Word word : choiceObjects) {
                choices.add(word.getWord());
            }

            return getFlashcardResponseEntity(response, correct, choices);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> spelling(String authHeader, Long tableId) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();
            long userID = userDAO.getUserID(username);

            Word correct;
            if (tableId == null) {
                correct = wordDAO.getRandomWordFromAll(userID);
            } else {
                correct = wordDAO.getRandomWordFromTable(tableId);
            }

            if (correct == null) {
                response.put("error", "No more words left to learn");
                return ResponseEntity.ok(response);
            }

            List<String> choiceStrings = IncorrectWordHeuristics.getIncorrectWords(correct.getWord());

            choiceStrings.add(correct.getWord());
            Collections.shuffle(choiceStrings);

            return getFlashcardResponseEntity(response, correct, choiceStrings);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> image(String authHeader, Long tableId) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            Image image;
            if (tableId == null) {
                image = imageDAO.getRandomImage(userDAO.getUserID(username));
            } else {
                image = imageDAO.getRandomImageFromTable(tableId);
            }

            if (image == null ) {
                response.put("error", "No images found");
                return ResponseEntity.ok(response);
            }
            String filename = image.getImageName();
            Word word = wordDAO.getWordWithId(image.getWordId());


            long flashcardId = UniqueIdGenerator.getInstance().generateUniqueId();
            FlashcardAnswers.getInstance().add(flashcardId, word);
            response.put("id", flashcardId);
            response.put("filename", filename);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> answer(String authHeader, String guess, long flashcard_id) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            if(!FlashcardAnswers.getInstance().contains(flashcard_id)) {
                return ResponseEntity.notFound().build();
            }

            Word correctAnswer = FlashcardAnswers.getInstance().getAnswer(flashcard_id);
            FlashcardAnswers.getInstance().remove(flashcard_id);
            long userId = userDAO.getUserID(username);
            long wordId = correctAnswer.getId();
            boolean isCorrect = correctAnswer.getWord().equals(guess);

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
                wordDAO.learnWord(userId, wordId);
            }

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private ResponseEntity<?> getFlashcardResponseEntity(Map<String, Object> response, Word correct, List<String> choiceStrings) {
        long flashcardId = UniqueIdGenerator.getInstance().generateUniqueId();
        FlashcardAnswers.getInstance().add(flashcardId, correct);

        response.put("id", flashcardId);
        response.put("question", correct.getDefinition());
        response.put("choices", choiceStrings);

        return ResponseEntity.ok(response);
    }
}