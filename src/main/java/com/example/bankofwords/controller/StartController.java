package com.example.bankofwords.controller;

import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Word;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@org.springframework.stereotype.Controller
public class StartController {

    private final WordDAO wordDAO;
    private final UserDAO userDAO;

    @Autowired
    public StartController(WordDAO wordDAO, UserDAO userDAO) {
        this.wordDAO = wordDAO;
        this.userDAO = userDAO;
    }

    @GetMapping("/start")
    public String start(@RequestParam(value = "showResult", required = false) Boolean showResult,
                        HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        if (username == null)
            return "redirect:/login";

        model.addAttribute("username", username);

        int id = userDAO.getUserID(username);

        Word correct = wordDAO.getRandomWord(id);

        List<Word> choices = wordDAO.getIncorrectWords(correct, id);

        choices.add(correct);
        Collections.shuffle(choices);

        model.addAttribute("correct", correct);
        model.addAttribute("choices", choices);

        Boolean result = (Boolean) session.getAttribute("result");
        if (showResult != null && showResult && result != null) {
            model.addAttribute("result", result);
            session.removeAttribute("result");
        }

        return "start";
    }


    @PostMapping("/check")
    public String checkAnswer(@RequestParam("selectedWord") String selectedWord,
                              @RequestParam("correctWord") String correctWord,
                              HttpSession session) {
        boolean guessedCorrect = selectedWord.equals(correctWord);
        session.setAttribute("result", guessedCorrect);

        // Check if the answer is correct and redirect accordingly
        if (guessedCorrect) {
            return "redirect:/start";
        } else {
            // Show the result and stay on the same page
            return "redirect:/start?showResult=true";
        }
    }


}
