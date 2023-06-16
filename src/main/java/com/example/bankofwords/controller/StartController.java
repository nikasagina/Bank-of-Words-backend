
package com.example.bankofwords.controller;

import com.example.bankofwords.constants.StatisticsConstants;
import com.example.bankofwords.dao.StatisticsDAO;
import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.singletons.FlashcardAnswers;
import com.example.bankofwords.singletons.UniqueIdGenerator;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("api")
public class StartController {

    private final WordDAO wordDAO;
    private final UserDAO userDAO;
    private final JwtUtil jwtUtil;
    private final StatisticsDAO statisticsDAO;

    @Autowired
    public StartController(WordDAO wordDAO, UserDAO userDAO, StatisticsDAO statisticsDAO, JwtUtil jwtUtil) {
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

            Word correct = wordDAO.getRandomWord(userID);

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

            long flashcardId = UniqueIdGenerator.getInstance().generateUniqueId();
            FlashcardAnswers.getInstance().add(flashcardId, correct.getWord());

            response.put("id", flashcardId);
            response.put("question", correct.getDefinition());
            response.put("choices", choices);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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
                    statisticsDAO.getSuccessRate(userId, wordId) > StatisticsConstants.MINIMUM_LEARN_RATE) {
                wordDAO.learnWord(userId, wordId);
            }

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/learn")
    public ResponseEntity<?> learn(@RequestHeader("Authorization") String authHeader, @RequestParam("word") String word) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);
        if (jwtUtil.validateToken(token, username)) {
            Map<String, Object> response = new HashMap<>();

            long userId = userDAO.getUserID(username);
            long wordId = wordDAO.getWordId(word, userId);

            if (wordDAO.alreadyKnows(userId, wordId)) {
                response.put("success", false);
            } else {
                wordDAO.learnWord(userId, wordId);
                response.put("success", true);
            }

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
