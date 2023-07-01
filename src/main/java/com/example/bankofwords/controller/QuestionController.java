
package com.example.bankofwords.controller;

import com.example.bankofwords.constants.StatisticsConstants;
import com.example.bankofwords.dao.StatisticsDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.singletons.FlashcardAnswers;
import com.example.bankofwords.singletons.UniqueIdGenerator;
import com.example.bankofwords.utils.IncorrectWordHeuristics;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("api")
public class QuestionController {

    private final WordDAO wordDAO;
    private final UserDAO userDAO;
    private final JwtUtil jwtUtil;
    private final StatisticsDAO statisticsDAO;

    @Autowired
    public QuestionController(WordDAO wordDAO, UserDAO userDAO, StatisticsDAO statisticsDAO, JwtUtil jwtUtil) {
        this.wordDAO = wordDAO;
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
        this.statisticsDAO = statisticsDAO;
    }

    @GetMapping("/question")
    public ResponseEntity<?> start(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();
            long userID = userDAO.getUserID(username);



            Word correct = null; // use algorithm to generate word to serve
            double randNum = new Random().nextDouble();
            if (randNum < StatisticsConstants.LEARNING_WORD_SERVE_RATE){
                correct = wordDAO.getRandomWordWithProgress(userID);
            }

            // handles both cases: 1. algorithm should generate random word and 2. user has no progress on any words
            if (correct == null){
                correct = wordDAO.getRandomWord(userID);
            }

            if (correct == null) {
                response.put("error", "No more words left to learn");
                return ResponseEntity.ok(response);
            }

            List<Word> choiceObjects = wordDAO.getIncorrectWords(correct, userID);

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

    @GetMapping("/question/spelling")
    public ResponseEntity<?> spelling(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();
            long userID = userDAO.getUserID(username);
            Word correct = wordDAO.getRandomWord(userID);

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

    private ResponseEntity<?> getFlashcardResponseEntity(Map<String, Object> response, Word correct, List<String> choiceStrings) {
        long flashcardId = UniqueIdGenerator.getInstance().generateUniqueId();
        FlashcardAnswers.getInstance().add(flashcardId, correct.getWord());

        response.put("id", flashcardId);
        response.put("question", correct.getDefinition());
        response.put("choices", choiceStrings);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/answer")
    public ResponseEntity<?> answer(@RequestHeader("Authorization") String authHeader,
                                    @RequestParam("guess") String guess,
                                    @RequestParam("id") long flashcard_id) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            if(!FlashcardAnswers.getInstance().contains(flashcard_id)) {
                return ResponseEntity.notFound().build();
            }

            String correctAnswer = FlashcardAnswers.getInstance().getAnswer(flashcard_id);
            FlashcardAnswers.getInstance().remove(flashcard_id);
            long userId = userDAO.getUserID(username);
            long wordId = wordDAO.getWordId(correctAnswer, userId);
            boolean isCorrect = correctAnswer.equals(guess);

            response.put("correct", isCorrect);
            response.put("answer", correctAnswer);

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
}
