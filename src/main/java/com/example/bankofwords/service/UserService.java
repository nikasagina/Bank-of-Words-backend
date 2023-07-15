package com.example.bankofwords.service;

import com.example.bankofwords.dao.UserDAO;
import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.User;
import com.example.bankofwords.objects.Word;
import com.example.bankofwords.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserDAO userDAO;
    private final WordDAO wordDAO;

    @Autowired
    public UserService(UserDAO userDAO, WordDAO wordDAO) {
        this.wordDAO = wordDAO;
        this.userDAO = userDAO;
    }

    public User getInfo() {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        return userDAO.getUserById(userId);
    }

    public List<Word>  getAllLearningWords() {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        return wordDAO.getAllLearningWords(userId);
    }

    public List<Word> getAllLearnedWords() {
        Long userId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        return wordDAO.getAllLearnedWords(userId);
    }
}
